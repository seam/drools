/*
 * JBoss, Home of Professional Open Source
 * Copyright ${year}, Red Hat, Inc., and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.seam.drools;

import java.io.InputStream;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringReader;
import java.util.Iterator;
import java.util.Map.Entry;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.New;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;

import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.base.evaluators.EvaluatorDefinition;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderConfiguration;
import org.drools.builder.KnowledgeBuilderError;
import org.drools.builder.KnowledgeBuilderErrors;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.builder.conf.EvaluatorOption;
import org.drools.event.knowledgebase.KnowledgeBaseEventListener;
import org.drools.io.ResourceFactory;
import org.drools.template.ObjectDataCompiler;
import org.jboss.seam.drools.bootstrap.DroolsExtension;
import org.jboss.seam.drools.config.DroolsConfig;
import org.jboss.seam.drools.config.DroolsConfigUtil;
import org.jboss.seam.drools.config.RuleResource;
import org.jboss.seam.drools.config.RuleResources;
import org.jboss.seam.drools.events.KnowledgeBuilderErrorsEvent;
import org.jboss.seam.drools.events.RuleResourceAddedEvent;
import org.jboss.weld.extensions.bean.generic.Generic;
import org.jboss.weld.extensions.bean.generic.GenericBean;
import org.jboss.weld.extensions.bean.generic.GenericProduct;
import org.jboss.weld.extensions.core.Exact;
import org.jboss.weld.extensions.resourceLoader.ResourceProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author Tihomir Surdilovic
 */
@Dependent
@Generic(DroolsConfig.class)
public class KnowledgeBaseProducer implements Serializable
{
   private static final Logger log = LoggerFactory.getLogger(KnowledgeBaseProducer.class);

   @Inject
   BeanManager manager;

   @Inject
   ResourceProvider resourceProvider;

   @Inject
   DroolsExtension droolsExtension;

   @Inject
   DroolsConfig config;

   @Inject
   @GenericBean
   DroolsConfigUtil configUtils;

   @Produces
   @ApplicationScoped
   @Default
   public KnowledgeBase produceKnowledgeBase() throws Exception
   {
      KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder(addCustomEvaluators(configUtils.getKnowledgeBuilderConfiguration()));

      if (config.ruleResources().length == 0)
      {
         throw new IllegalStateException("No rule resources are specified.");
      }

      for (RuleResource resourceEntry : config.ruleResources())
      {
         System.out.println(config.ruleResources());
         addResource(kbuilder, resourceEntry.value());
      }

      KnowledgeBuilderErrors kbuildererrors = kbuilder.getErrors();
      if (kbuildererrors.size() > 0)
      {
         for (KnowledgeBuilderError kbuildererror : kbuildererrors)
         {
            log.error(kbuildererror.getMessage());
         }
         manager.fireEvent(new KnowledgeBuilderErrorsEvent(kbuildererrors));
      }

      KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase(configUtils.getKnowledgeBaseConfiguration());
      kbase.addKnowledgePackages(kbuilder.getKnowledgePackages());

      addEventListeners(kbase);

      return kbase;
   }

   private KnowledgeBuilderConfiguration addCustomEvaluators(KnowledgeBuilderConfiguration config)
   {
      Iterator<Entry<String, EvaluatorDefinition>> allCustomEvaluators = droolsExtension.getEvaluatorDefinitions().entrySet().iterator();
      while (allCustomEvaluators.hasNext())
      {
         Entry<String, EvaluatorDefinition> nextEvalInfo = allCustomEvaluators.next();
         config.setOption(EvaluatorOption.get(nextEvalInfo.getKey(), nextEvalInfo.getValue()));
      }
      return config;
   }

   private void addEventListeners(KnowledgeBase kbase)
   {
      Iterator<KnowledgeBaseEventListener> allKBaseEventListeners = droolsExtension.getKbaseEventListenerSet().iterator();
      while (allKBaseEventListeners.hasNext())
      {
         KnowledgeBaseEventListener listener = allKBaseEventListeners.next();
         kbase.addEventListener(listener);
         log.info("Added KnowledgeBaseEventListener: " + listener);
      }
   }

   private void addResource(KnowledgeBuilder kbuilder, String entry) throws Exception
   {
      String[] entryParts = RuleResources.DIVIDER.split(entry.trim());

      if (entryParts.length >= 3)
      {
         ResourceType resourceType = ResourceType.getResourceType(entryParts[RuleResources.RESOURCE_TYPE]);

         if (entryParts.length == 4)
         {
            TemplateDataProvider templateDataProvider = droolsExtension.getTemplateDataProviders().get(entryParts[RuleResources.TEMPLATE_DATAPROVIDER_NAME]);
            if (templateDataProvider != null)
            {
               InputStream templateStream = resourceProvider.loadResourceStream(entryParts[RuleResources.RESOURCE_PATH]);
               if (templateStream == null)
               {
                  throw new IllegalStateException("Could not load rule template: " + entryParts[RuleResources.RESOURCE_PATH]);
               }
               ObjectDataCompiler converter = new ObjectDataCompiler();
               String drl = converter.compile(templateDataProvider.getTemplateData(), templateStream);
               log.info("Generated following rule from template and template data: \n" + drl);
               templateStream.close();
               Reader rdr = new StringReader(drl);
               kbuilder.add(ResourceFactory.newReaderResource(rdr), resourceType);
            }
            else
            {
               throw new IllegalStateException("Requested template data provider: " + entryParts[RuleResources.TEMPLATE_DATAPROVIDER_NAME] + " for resource " + entryParts[RuleResources.RESOURCE_PATH] + " has not been created. Check to make sure you have defined one.");
            }
         }
         else
         {
            if (entryParts[RuleResources.LOCATION_TYPE].equals(RuleResources.LOCATION_TYPE_URL))
            {
               kbuilder.add(ResourceFactory.newUrlResource(entryParts[RuleResources.RESOURCE_PATH]), resourceType);
               manager.fireEvent(new RuleResourceAddedEvent(entryParts[RuleResources.RESOURCE_PATH]));
            }
            else if (entryParts[RuleResources.LOCATION_TYPE].equals(RuleResources.LOCATION_TYPE_FILE))
            {
               kbuilder.add(ResourceFactory.newFileResource(entryParts[RuleResources.RESOURCE_PATH]), resourceType);
               manager.fireEvent(new RuleResourceAddedEvent(entryParts[RuleResources.RESOURCE_PATH]));
            }
            else if (entryParts[RuleResources.LOCATION_TYPE].equals(RuleResources.LOCATION_TYPE_CLASSPATH))
            {
               kbuilder.add(ResourceFactory.newClassPathResource(entryParts[RuleResources.RESOURCE_PATH]), resourceType);
               manager.fireEvent(new RuleResourceAddedEvent(entryParts[RuleResources.RESOURCE_PATH]));
            }
            else
            {
               log.error("Invalid resource: " + entryParts[RuleResources.RESOURCE_PATH]);
            }
         }
      }
      else
      {
         log.error("Invalid resource entry definition: " + entry);
      }
   }
}
