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

import java.util.Properties;

import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;

import org.drools.KnowledgeBase;
import org.drools.agent.KnowledgeAgent;
import org.drools.agent.KnowledgeAgentConfiguration;
import org.drools.agent.KnowledgeAgentFactory;
import org.drools.builder.ResourceType;
import org.drools.io.ResourceChangeScannerConfiguration;
import org.drools.io.ResourceFactory;
import org.jboss.seam.drools.config.DroolsConfiguration;
import org.jboss.seam.drools.events.RuleResourceAddedEvent;
import org.jboss.seam.drools.qualifiers.Scanned;
import org.jboss.seam.drools.utils.ConfigUtils;
import org.jboss.weld.extensions.resources.ResourceProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author Tihomir Surdilovic
 */
public class KnowledgeAgentProducer
{
   private static final Logger log = LoggerFactory.getLogger(KnowledgeAgentProducer.class);

   @Inject
   BeanManager manager;
   @Inject
   ResourceProvider resourceProvider;

   @Produces
   @Scanned
   public KnowledgeBase produceScannedKnowledgeBase(DroolsConfiguration kagentConfig) throws Exception
   {
      ResourceFactory.getResourceChangeScannerService().configure(getResourceChangeScannerConfig(kagentConfig.getResourceChangeScannerConfigPath()));
      KnowledgeAgentConfiguration aconf = getKnowledgeAgentConfiguration(kagentConfig.getKnowledgeAgentConfigPath());

      KnowledgeAgent kagent;
      kagent = KnowledgeAgentFactory.newKnowledgeAgent(kagentConfig.getKnowledgeAgentName(), aconf);
      applyChangeSet(kagent, kagentConfig.getChangeSetResource());
      
      if(kagentConfig.isStartChangeNotifierService()) {
         ResourceFactory.getResourceChangeNotifierService().start();
      }
      if(kagentConfig.isStartChangeScannerService()) {
         ResourceFactory.getResourceChangeScannerService().start();
      }
      
      return kagent.getKnowledgeBase();
      
   }
   
   public void disposeScannedKnowledgeBase(@Disposes @Scanned KnowledgeBase kbase) {
      ResourceFactory.getResourceChangeNotifierService().stop();
      ResourceFactory.getResourceChangeScannerService().stop();
   }
   
   private void applyChangeSet(KnowledgeAgent kagent, String changeSetResource) {
      if (ConfigUtils.isValidResource(changeSetResource))
      {
         ResourceType resourceType = ResourceType.getResourceType(ConfigUtils.getResourceType(changeSetResource));
         if(resourceType.equals(ResourceType.CHANGE_SET)) {
            if (ConfigUtils.getResourcePath(changeSetResource).equals(ConfigUtils.RESOURCE_TYPE_URL))
            {
               kagent.applyChangeSet(ResourceFactory.newUrlResource(ConfigUtils.getRuleResource(changeSetResource)));
               manager.fireEvent(new RuleResourceAddedEvent(ConfigUtils.getRuleResource(changeSetResource)));
            }
            else if (ConfigUtils.getResourcePath(changeSetResource).equals(ConfigUtils.RESOURCE_TYPE_FILE))
            {
               kagent.applyChangeSet(ResourceFactory.newFileResource(ConfigUtils.getRuleResource(changeSetResource)));
               manager.fireEvent(new RuleResourceAddedEvent(ConfigUtils.getRuleResource(changeSetResource)));
            }
            else if (ConfigUtils.getResourcePath(changeSetResource).equals(ConfigUtils.RESOURCE_TYPE_CLASSPATH))
            {
               kagent.applyChangeSet(ResourceFactory.newClassPathResource(ConfigUtils.getRuleResource(changeSetResource)));
               manager.fireEvent(new RuleResourceAddedEvent(ConfigUtils.getRuleResource(changeSetResource)));
            }
            else
            {
               log.error("Invalid resource: " + ConfigUtils.getResourcePath(changeSetResource));
            }
         } else {
            log.error("Resource must be of type CHANGE_SET");
         }
      } else {
         log.error("Invalid resource definition: " + changeSetResource);
      }
      
      
   }

   private ResourceChangeScannerConfiguration getResourceChangeScannerConfig(String resourceChangeScannerConfigPath) throws Exception
   {
      ResourceChangeScannerConfiguration sconf = ResourceFactory.getResourceChangeScannerService().newResourceChangeScannerConfiguration();

      if (resourceChangeScannerConfigPath != null && resourceChangeScannerConfigPath.endsWith(".properties"))
      {
         Properties resourceChangeScannerConfProp = ConfigUtils.loadProperties(resourceProvider, resourceChangeScannerConfigPath);
         sconf = ResourceFactory.getResourceChangeScannerService().newResourceChangeScannerConfiguration(resourceChangeScannerConfProp);
         log.debug("ResourceChangeScannerConfiguration loaded: " + resourceChangeScannerConfigPath);
      }
      else
      {
         log.warn("Invalid config type: " + resourceChangeScannerConfigPath);
      }

      return sconf;
   }

   private KnowledgeAgentConfiguration getKnowledgeAgentConfiguration(String knowledgeAgentConfigPath) throws Exception
   {
      KnowledgeAgentConfiguration aconf = KnowledgeAgentFactory.newKnowledgeAgentConfiguration();

      if (knowledgeAgentConfigPath != null && knowledgeAgentConfigPath.endsWith(".properties"))
      {
         Properties knowledgeAgentConfProp = ConfigUtils.loadProperties(resourceProvider, knowledgeAgentConfigPath);
         aconf = KnowledgeAgentFactory.newKnowledgeAgentConfiguration(knowledgeAgentConfProp);
         log.debug("KnowledgeAgentConfiguration loaded: " + knowledgeAgentConfigPath);
      }
      else
      {
         log.warn("Invalid config type: " + knowledgeAgentConfigPath);
      }

      return aconf;
   }
}
