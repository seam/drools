package org.jboss.seam.drools.config;

/**
 * Configuration data for Drools KnowledeAgent.
 * 
 * @author Tihomir Surdilovic
 */
public class KnowledgeAgentConfig
{
   private String resourceChangeScannerConfigPath;
   private String knowledgeAgentConfigPath;
   private String changeSetResource;
   private String name;
   private boolean startChangeNotifierService;
   private boolean startChangeScannerService;
   
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

   public String getName()
   {
      return name;
   }

   public void setName(String name)
   {
      this.name = name;
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

}
