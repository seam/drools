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
   
   public static final String RESOURCE_TYPE_URL = "url";
   public static final String RESOURCE_TYPE_FILE = "file";
   public static final String RESOURCE_TYPE_CLASSPATH = "classpath";

   private String knowledgeBuilderConfig;
   private String knowledgeBaseConfig;
   private String[] ruleResources;
   private String[] eventListeners;
   
   public static boolean isValidResource(String resource) {
      return DIVIDER.split(resource.trim()).length >= 3;
   }
   
   public static boolean isRuleTemplate(String resource) {
      return DIVIDER.split(resource.trim()).length == 4;
   }
   
   public static String getResourceType(String resource) {
      return DIVIDER.split(resource.trim())[RESOURCE_TYPE];
   }
   
   public static String getRuleResource(String resource) {
      return DIVIDER.split(resource.trim())[RESOURCE];
   }
   
   public static String getTemplateData(String resource) {
      return DIVIDER.split(resource.trim())[RESOURCE_TEMPLATE_DATA];
   }
   
   public static String getResourcePath(String resource) {
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
