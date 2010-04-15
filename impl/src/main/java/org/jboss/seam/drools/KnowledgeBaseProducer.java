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
import java.io.StringReader;
import java.util.Iterator;
import java.util.Properties;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;

import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseConfiguration;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderConfiguration;
import org.drools.builder.KnowledgeBuilderError;
import org.drools.builder.KnowledgeBuilderErrors;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.event.knowledgebase.KnowledgeBaseEventListener;
import org.drools.io.ResourceFactory;
import org.drools.template.ObjectDataCompiler;
import org.jboss.seam.drools.bootstrap.DroolsExtension;
import org.jboss.seam.drools.config.DroolsConfiguration;
import org.jboss.seam.drools.events.KnowledgeBuilderErrorsEvent;
import org.jboss.seam.drools.events.RuleResourceAddedEvent;
import org.jboss.seam.drools.utils.ConfigUtils;
import org.jboss.weld.extensions.resources.ResourceProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author Tihomir Surdilovic
 */
public class KnowledgeBaseProducer
{
   private static final Logger log = LoggerFactory.getLogger(KnowledgeBaseProducer.class);

   @Inject
   BeanManager manager;
   
   @Inject
   ResourceProvider resourceProvider;
   
   @Inject
   DroolsExtension droolsExtension;

   @Produces
   public KnowledgeBase produceKnowledgeBase(DroolsConfiguration kbaseConfig) throws Exception
   {
      KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder(getKnowledgeBuilderConfiguration(kbaseConfig.getKnowledgeBuilderConfigPath()));

      for (String nextResource : kbaseConfig.getRuleResources())
      {
         addResource(kbuilder, nextResource);
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

      KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase(getKnowledgeBaseConfiguration(kbaseConfig.getKnowledgeBaseConfigPath()));
      kbase.addKnowledgePackages(kbuilder.getKnowledgePackages());

      addEventListeners(kbase);

      return kbase;
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

   private KnowledgeBuilderConfiguration getKnowledgeBuilderConfiguration(String knowledgeBuilderConfigPath) throws Exception
   {
      KnowledgeBuilderConfiguration droolsKbuilderConfig = KnowledgeBuilderFactory.newKnowledgeBuilderConfiguration();
      if (knowledgeBuilderConfigPath != null && knowledgeBuilderConfigPath.endsWith(".properties"))
      {

         Properties kbuilderProp = ConfigUtils.loadProperties(resourceProvider, knowledgeBuilderConfigPath);
         droolsKbuilderConfig = KnowledgeBuilderFactory.newKnowledgeBuilderConfiguration(kbuilderProp, null);
         log.debug("KnowledgeBuilderConfiguration loaded: " + knowledgeBuilderConfigPath);
      }
      else
      {
         log.warn("Invalid config type: " + knowledgeBuilderConfigPath);
      }
      return droolsKbuilderConfig;
   }

   public KnowledgeBaseConfiguration getKnowledgeBaseConfiguration(String knowledgeBaseConfigPath) throws Exception
   {
      KnowledgeBaseConfiguration droolsKbaseConfig = KnowledgeBaseFactory.newKnowledgeBaseConfiguration();
      if (knowledgeBaseConfigPath != null && knowledgeBaseConfigPath.endsWith(".properties"))
      {
         Properties kbaseProp = ConfigUtils.loadProperties(resourceProvider, knowledgeBaseConfigPath);
         droolsKbaseConfig = KnowledgeBaseFactory.newKnowledgeBaseConfiguration(kbaseProp, null);
         log.debug("KnowledgeBaseConfiguration loaded: " + knowledgeBaseConfigPath);
      }
      return droolsKbaseConfig;
   }

   private void addResource(KnowledgeBuilder kbuilder, String resource) throws Exception
   {
      if (ConfigUtils.isValidResource(resource))
      {
         ResourceType resourceType = ResourceType.getResourceType(ConfigUtils.getResourceType(resource));

         if (ConfigUtils.isRuleTemplate(resource))
         {
            TemplateDataProvider templateDataProvider = droolsExtension.getTemplateDataProviders().get(ConfigUtils.getTemplateData(resource));
            if (templateDataProvider != null)
            {
               InputStream templateStream = resourceProvider.loadResourceStream(ConfigUtils.getRuleResource(resource));
               if (templateStream == null)
               {
                  throw new IllegalStateException("Could not load rule template: " + ConfigUtils.getRuleResource(resource));
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
               throw new IllegalStateException("Requested template data provider: " + ConfigUtils.getTemplateData(resource) + " for resource " + resource + " has not been created. Check to make sure you have defined one.");
            }
         }
         else
         {
            if (ConfigUtils.getResourcePath(resource).equals(ConfigUtils.RESOURCE_TYPE_URL))
            {
               kbuilder.add(ResourceFactory.newUrlResource(ConfigUtils.getRuleResource(resource)), resourceType);
               manager.fireEvent(new RuleResourceAddedEvent(ConfigUtils.getRuleResource(resource)));
            }
            else if (ConfigUtils.getResourcePath(resource).equals(ConfigUtils.RESOURCE_TYPE_FILE))
            {
               kbuilder.add(ResourceFactory.newFileResource(ConfigUtils.getRuleResource(resource)), resourceType);
               manager.fireEvent(new RuleResourceAddedEvent(ConfigUtils.getRuleResource(resource)));
            }
            else if (ConfigUtils.getResourcePath(resource).equals(ConfigUtils.RESOURCE_TYPE_CLASSPATH))
            {
               kbuilder.add(ResourceFactory.newClassPathResource(ConfigUtils.getRuleResource(resource)), resourceType);
               manager.fireEvent(new RuleResourceAddedEvent(ConfigUtils.getRuleResource(resource)));
            }
            else
            {
               log.error("Invalid resource: " + ConfigUtils.getResourcePath(resource));
            }
         }
      }
      else
      {
         log.error("Invalid resource definition: " + resource);
      }
   }
}
