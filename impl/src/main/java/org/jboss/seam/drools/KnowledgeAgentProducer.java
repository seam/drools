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
import org.jboss.seam.drools.config.KnowledgeAgentConfig;
import org.jboss.seam.drools.events.RuleResourceAddedEvent;
import org.jboss.seam.drools.qualifiers.kbase.KAgentConfigured;
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
   @KAgentConfigured
   KnowledgeBase produceAgentKBase(KnowledgeAgentConfig kagentConfig) throws Exception
   {
      ResourceFactory.getResourceChangeScannerService().configure(getResourceChangeScannerConfig(kagentConfig.getResourceChangeScannerConfigPath()));
      KnowledgeAgentConfiguration aconf = getKnowledgeAgentConfiguration(kagentConfig.getKnowledgeAgentConfigPath());

      KnowledgeAgent kagent;
      kagent = KnowledgeAgentFactory.newKnowledgeAgent(kagentConfig.getName(), aconf);
      applyChangeSet(kagent, kagentConfig.getChangeSetResource());
      
      if(kagentConfig.isStartChangeNotifierService()) {
         ResourceFactory.getResourceChangeNotifierService().start();
      }
      if(kagentConfig.isStartChangeScannerService()) {
         ResourceFactory.getResourceChangeScannerService().start();
      }
      
      return kagent.getKnowledgeBase();
      
   }
   
   public void disposeAgentKBase(@Disposes @KAgentConfigured KnowledgeBase kbase) {
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
