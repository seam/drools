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
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;

import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.base.evaluators.EvaluatorDefinition;
import org.drools.builder.DecisionTableConfiguration;
import org.drools.builder.DecisionTableInputType;
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
import org.jboss.seam.drools.config.Drools;
import org.jboss.seam.drools.config.RuleResource;
import org.jboss.seam.drools.config.RuleResources;
import org.jboss.seam.drools.configutil.DroolsConfigUtil;
import org.jboss.seam.drools.events.KnowledgeBuilderErrorsEvent;
import org.jboss.seam.drools.events.RuleResourceAddedEvent;
import org.jboss.seam.solder.bean.generic.Generic;
import org.jboss.seam.solder.resourceLoader.ResourceProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author Tihomir Surdilovic
 */
@Dependent
@Generic
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
   //@GenericBean
   Drools drools;
   
   @Inject
   //@GenericBean
   DroolsConfigUtil configUtils;
   
   @Inject 
   //@GenericProduct
   RuleResources ruleResources;
   
   
   @Produces
   @ApplicationScoped
   public KnowledgeBase produceKnowledgeBase() throws Exception
   {
      KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder(addCustomEvaluators(configUtils.getKnowledgeBuilderConfiguration()));
      
      Iterator<RuleResource> resourceIterator = ruleResources.iterator();
      while(resourceIterator.hasNext()) {
         addResource(kbuilder, resourceIterator.next());
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

   private void addResource(KnowledgeBuilder kbuilder, RuleResource resource) throws Exception
   {
         ResourceType resourceType = ResourceType.getResourceType(resource.getType());
         if (!isEmpty(resource.getTemplateData()))
         {
            TemplateDataProvider templateDataProvider = droolsExtension.getTemplateDataProviders().get(resource.getTemplateData());
            if (templateDataProvider != null)
            {
               InputStream templateStream = resource.getDroolsResouce().getInputStream();
               if (templateStream == null)
               {
                  throw new IllegalStateException("Could not load rule template: " + resource.getFullPath());
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
               throw new IllegalStateException("Requested template data provider: " + resource.getTemplateData() + " for resource " + resource.getFullPath() + " has not been created. Check to make sure you have defined one.");
            }
         }
         else
         {
            if(resourceType == ResourceType.DTABLE) {
               DecisionTableConfiguration dtconf = KnowledgeBuilderFactory.newDecisionTableConfiguration();
               if(!isEmpty(resource.getDtType())) {
                  dtconf.setInputType( DecisionTableInputType.valueOf(resource.getDtType()) );
               } else {
                  dtconf.setInputType( DecisionTableInputType.XLS );
               }
               if(!isEmpty(resource.getDtWorksheetName())) {
                  dtconf.setWorksheetName( resource.getDtWorksheetName() );
               }
               kbuilder.add( resource.getDroolsResouce(),
                                   resourceType,
                                   dtconf );
               manager.fireEvent(new RuleResourceAddedEvent(resource.getFullPath()));
            } else {
               kbuilder.add( resource.getDroolsResouce(), resourceType);
               manager.fireEvent(new RuleResourceAddedEvent(resource.getFullPath()));
            }
         }
      }
      
   private boolean isEmpty(String value) {
      return (value == null || value.trim().length() <= 0);
   }
}
