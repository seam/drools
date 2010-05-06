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

import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;

import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.agent.KnowledgeAgent;
import org.drools.agent.KnowledgeAgentFactory;
import org.drools.builder.ResourceType;
import org.drools.io.ResourceFactory;
import org.jboss.seam.drools.config.DroolsConfig;
import org.jboss.seam.drools.config.RuleResources;
import org.jboss.seam.drools.qualifiers.Scanned;
import org.jboss.weld.extensions.resourceLoader.ResourceProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * KnowledgeAgent producer.
 * 
 * @author Tihomir Surdilovic
 */
@ApplicationScoped
public class KnowledgeAgentProducer implements Serializable
{
   private static final Logger log = LoggerFactory.getLogger(KnowledgeAgentProducer.class);

   @Inject
   BeanManager manager;
   
   @Inject
   ResourceProvider resourceProvider;

   @Produces
   @ApplicationScoped
   public KnowledgeAgent produceKnowledgeAgent(DroolsConfig config) throws Exception
   {
      return getAgent(config);
   }
   
   @Produces
   @Scanned
   @ApplicationScoped
   public KnowledgeBase produceScannedKnowledgeBase(DroolsConfig config) throws Exception
   {
      KnowledgeAgent agent = getAgent(config);
      return agent.getKnowledgeBase();
   }
   
   private KnowledgeAgent getAgent(DroolsConfig config) throws Exception
   {
      if (config.getAgentName() == null || config.getAgentName().length() < 1)
      {
         throw new IllegalStateException("KnowledgeAgent configuration does not specify the name of the KnowlegeAgent.");
      }

      if(config.getRuleResources().getResources() == null || config.getRuleResources().getResources().length == 0) 
      {
         throw new IllegalStateException("No change set rule resource specified.");
      }
      
      if(config.getRuleResources().getResources().length > 1) {
         throw new IllegalStateException("More than one change set rule resource specified for KnowledgeAgent. Make sure only a single change set resource is specified.");
      }

      ResourceFactory.getResourceChangeScannerService().configure(config.getResourceChangeScannerConfiguration());

      KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase(config.getKnowledgeBaseConfiguration());
      KnowledgeAgent kagent = KnowledgeAgentFactory.newKnowledgeAgent(config.getAgentName(), kbase, config.getKnowledgeAgentConfiguration());

      applyChangeSet(kagent, config.getRuleResources().getResources()[0]);

      if (config.isStartChangeNotifierService())
      {
         ResourceFactory.getResourceChangeNotifierService().start();
      }
      if (config.isStartChangeScannerService())
      {
         ResourceFactory.getResourceChangeScannerService().start();
      }

      return kagent;

   }

   public void disposeScannedKnowledgeBase(@Disposes @Scanned KnowledgeBase kbase)
   {
      // do we really want to stop ?
      ResourceFactory.getResourceChangeNotifierService().stop();
      ResourceFactory.getResourceChangeScannerService().stop();
   }
   
   private void applyChangeSet(KnowledgeAgent kagent, String entry)
   {
      String[] entryParts = RuleResources.DIVIDER.split(entry.trim());
      
      if (entryParts.length >= 3)
      {
         ResourceType resourceType = ResourceType.getResourceType(entryParts[RuleResources.RESOURCE_TYPE]);
         if (resourceType.equals(ResourceType.CHANGE_SET))
         {
            if (entryParts[RuleResources.LOCATION_TYPE].equals(RuleResources.LOCATION_TYPE_URL))
            {
               kagent.applyChangeSet(ResourceFactory.newUrlResource(entryParts[RuleResources.RESOURCE_PATH]));
            }
            else if (entryParts[RuleResources.LOCATION_TYPE].equals(RuleResources.LOCATION_TYPE_FILE))
            {
               kagent.applyChangeSet(ResourceFactory.newFileResource(entryParts[RuleResources.RESOURCE_PATH]));
            }
            else if (entryParts[RuleResources.LOCATION_TYPE].equals(RuleResources.LOCATION_TYPE_CLASSPATH))
            {
               kagent.applyChangeSet(ResourceFactory.newClassPathResource(entryParts[RuleResources.RESOURCE_PATH]));
            }
            else
            {
               log.error("Invalid resource: " + entry);
            }
         }
         else
         {
            log.error("Resource must be of type CHANGE_SET");
         }
      }
      else
      {
         log.error("Invalid resource definition: " + entry);
      }
   }

}
