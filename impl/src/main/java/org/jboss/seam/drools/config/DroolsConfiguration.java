package org.jboss.seam.drools.config;

import org.jboss.seam.drools.qualifiers.config.DroolsConfig;

@DroolsConfig
public class DroolsConfiguration
{
   //KnowledgeBase configuration
   private String knowledgeBuilderConfigPath;
   private String knowledgeBaseConfigPath;
   private String[] ruleResources;
   //KnowledgeSession configuration
   private String knowledgeSessionConfigPath;
   //KnowledgeAgent configuration
   private String resourceChangeScannerConfigPath;
   private String knowledgeAgentConfigPath;
   private String changeSetResource;
   private String knowledgeAgentName;
   private boolean startChangeNotifierService;
   private boolean startChangeScannerService;
   //KnowledgeLogger configuration
   private String loggerName;
   private String loggerType;
   private String loggerPath;
   private int loggerInterval;
   
   public String getKnowledgeBuilderConfigPath()
   {
      return knowledgeBuilderConfigPath;
   }
   public void setKnowledgeBuilderConfigPath(String knowledgeBuilderConfigPath)
   {
      this.knowledgeBuilderConfigPath = knowledgeBuilderConfigPath;
   }
   public String getKnowledgeBaseConfigPath()
   {
      return knowledgeBaseConfigPath;
   }
   public void setKnowledgeBaseConfigPath(String knowledgeBaseConfigPath)
   {
      this.knowledgeBaseConfigPath = knowledgeBaseConfigPath;
   }
   public String[] getRuleResources()
   {
      return ruleResources;
   }
   public void setRuleResources(String[] ruleResources)
   {
      this.ruleResources = ruleResources;
   }
   public String getKnowledgeSessionConfigPath()
   {
      return knowledgeSessionConfigPath;
   }
   public void setKnowledgeSessionConfigPath(String knowledgeSessionConfigPath)
   {
      this.knowledgeSessionConfigPath = knowledgeSessionConfigPath;
   }
   public String getResourceChangeScannerConfigPath()
   {
      return resourceChangeScannerConfigPath;
   }
   public void setResourceChangeScannerConfigPath(String resourceChangeScannerConfigPath)
   {
      this.resourceChangeScannerConfigPath = resourceChangeScannerConfigPath;
   }
   public String getKnowledgeAgentConfigPath()
   {
      return knowledgeAgentConfigPath;
   }
   public void setKnowledgeAgentConfigPath(String knowledgeAgentConfigPath)
   {
      this.knowledgeAgentConfigPath = knowledgeAgentConfigPath;
   }
   public String getChangeSetResource()
   {
      return changeSetResource;
   }
   public void setChangeSetResource(String changeSetResource)
   {
      this.changeSetResource = changeSetResource;
   }
   public String getKnowledgeAgentName()
   {
      return knowledgeAgentName;
   }
   public void setKnowledgeAgentName(String knowledgeAgentName)
   {
      this.knowledgeAgentName = knowledgeAgentName;
   }
   public boolean isStartChangeNotifierService()
   {
      return startChangeNotifierService;
   }
   public void setStartChangeNotifierService(boolean startChangeNotifierService)
   {
      this.startChangeNotifierService = startChangeNotifierService;
   }
   public boolean isStartChangeScannerService()
   {
      return startChangeScannerService;
   }
   public void setStartChangeScannerService(boolean startChangeScannerService)
   {
      this.startChangeScannerService = startChangeScannerService;
   }
   public String getLoggerName()
   {
      return loggerName;
   }
   public void setLoggerName(String loggerName)
   {
      this.loggerName = loggerName;
   }
   public String getLoggerType()
   {
      return loggerType;
   }
   public void setLoggerType(String loggerType)
   {
      this.loggerType = loggerType;
   }
   public String getLoggerPath()
   {
      return loggerPath;
   }
   public void setLoggerPath(String loggerPath)
   {
      this.loggerPath = loggerPath;
   }
   public int getLoggerInterval()
   {
      return loggerInterval;
   }
   public void setLoggerInterval(int loggerInterval)
   {
      this.loggerInterval = loggerInterval;
   }
   
   
}
