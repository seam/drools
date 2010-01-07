package org.jboss.seam.drools;

import java.io.InputStream;
import java.util.Properties;
import java.util.regex.Pattern;

import org.drools.KnowledgeBaseConfiguration;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilderConfiguration;
import org.drools.builder.KnowledgeBuilderFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Drools KnowledgeBaseManager Configuration.
 * 
 * @author Tihomir Surdilovic
 */
public class KnowledgeBaseManagerConfig
{
   private static final Logger log = LoggerFactory.getLogger(KnowledgeBaseManagerConfig.class);

   private static final Pattern DIVIDER = Pattern.compile(";");
   private static final int RESOURCE_PATH = 0;
   private static final int RESOURCE = 1;
   private static final int RESOURCE_TYPE = 2;
   private static final int RESOURCE_TEMPLATE_DATA = 3;

   private String knowledgeBuilderConfig;
   private String knowledgeBaseConfig;
   private String[] ruleResources;
   private String[] eventListeners;

   public KnowledgeBuilderConfiguration getKnowledgeBuilderConfiguration() throws Exception
   {
      KnowledgeBuilderConfiguration kbuilderconfig = KnowledgeBuilderFactory.newKnowledgeBuilderConfiguration();
      // Only allow resource for .properties files
      if (knowledgeBuilderConfig != null && knowledgeBuilderConfig.endsWith(".properties"))
      {
         Properties kbuilderProp = new Properties();
         InputStream in = this.getClass().getClassLoader().getResourceAsStream(knowledgeBuilderConfig);
         if (in == null)
         {
            throw new IllegalStateException("Could not locate knowledgeBuilderConfig: " + knowledgeBuilderConfig);
         }
         kbuilderProp.load(in);
         in.close();
         kbuilderconfig = KnowledgeBuilderFactory.newKnowledgeBuilderConfiguration(kbuilderProp, null);
         log.debug("KnowledgeBuilderConfiguration loaded: " + knowledgeBuilderConfig);
      }
      return kbuilderconfig;
   }

   public KnowledgeBaseConfiguration getKnowledgeBaseConfiguration() throws Exception
   {
      KnowledgeBaseConfiguration kbaseconfig = KnowledgeBaseFactory.newKnowledgeBaseConfiguration();

      // Only allow resource for .properties files
      if (knowledgeBaseConfig != null && knowledgeBaseConfig.endsWith(".properties"))
      {
         Properties kbaseProp = new Properties();
         InputStream in = this.getClass().getClassLoader().getResourceAsStream(knowledgeBaseConfig);
         if (in == null)
         {
            throw new IllegalStateException("Could not locate knowledgeBaseConfig: " + knowledgeBaseConfig);
         }
         kbaseProp.load(in);
         in.close();
         kbaseconfig = KnowledgeBaseFactory.newKnowledgeBaseConfiguration(kbaseProp, null);
         log.debug("KnowledgeBaseConfiguration loaded: " + knowledgeBaseConfig);
      }
      return kbaseconfig;
   }
   
   public boolean isValidResource(String resource) {
      return DIVIDER.split(resource.trim()).length >= 3;
   }
   
   public boolean isRuleTemplate(String resource) {
      return DIVIDER.split(resource.trim()).length == 4;
   }
   
   public String getResourceType(String resource) {
      return DIVIDER.split(resource.trim())[RESOURCE_TYPE];
   }
   
   public String getRuleResource(String resource) {
      return DIVIDER.split(resource.trim())[RESOURCE];
   }
   
   public String getTemplateData(String resource) {
      return DIVIDER.split(resource.trim())[RESOURCE_TEMPLATE_DATA];
   }
   
   public String getResourcePath(String resource) {
      return DIVIDER.split(resource.trim())[RESOURCE_PATH];
   }

   public String getKnowledgeBuilderConfig()
   {
      return knowledgeBuilderConfig;
   }

   public void setKnowledgeBuilderConfig(String knowledgeBuilderConfig)
   {
      this.knowledgeBuilderConfig = knowledgeBuilderConfig;
   }

   public String getKnowledgeBaseConfig()
   {
      return knowledgeBaseConfig;
   }

   public void setKnowledgeBaseConfig(String knowledgeBaseConfig)
   {
      this.knowledgeBaseConfig = knowledgeBaseConfig;
   }

   public String[] getRuleResources()
   {
      return ruleResources;
   }

   public void setRuleResources(String[] ruleResources)
   {
      this.ruleResources = ruleResources;
   }

   public String[] getEventListeners()
   {
      return eventListeners;
   }

   public void setEventListeners(String[] eventListeners)
   {
      this.eventListeners = eventListeners;
   }

}
