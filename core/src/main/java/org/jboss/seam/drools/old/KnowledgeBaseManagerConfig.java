package org.jboss.seam.drools.old;

import java.util.Properties;
import java.util.regex.Pattern;

/**
 * KnowledgeBaseManager Configuration.
 * 
 * @author Tihomir Surdilovic
 */
public class KnowledgeBaseManagerConfig
{
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
   private Properties knowledgeBuilderConfigProp;
   private Properties knowledgeBaseConfigProp;
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

   public Properties getKnowledgeBuilderConfigProp()
   {
      return knowledgeBuilderConfigProp;
   }

   public void setKnowledgeBuilderConfigProp(Properties knowledgeBuilderConfigProp)
   {
      this.knowledgeBuilderConfigProp = knowledgeBuilderConfigProp;
   }

   public Properties getKnowledgeBaseConfigProp()
   {
      return knowledgeBaseConfigProp;
   }

   public void setKnowledgeBaseConfigProp(Properties knowledgeBaseConfigProp)
   {
      this.knowledgeBaseConfigProp = knowledgeBaseConfigProp;
   }
   
   

}
