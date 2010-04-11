package org.jboss.seam.drools.config;

/**
 * 
 * @author Tihomir Surdilovic
 */
public class KnowledgeBaseConfig
{
   private String knowledgeBuilderConfigPath;
   private String knowledgeBaseConfigPath;
   private String[] ruleResources;
   private String[] eventListeners;

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

   public String[] getEventListeners()
   {
      return eventListeners;
   }

   public void setEventListeners(String[] eventListeners)
   {
      this.eventListeners = eventListeners;
   }

   public String toString()
   {
      StringBuffer buff = new StringBuffer();
      buff.append("knowledgeBuilderConfigPath: " + knowledgeBuilderConfigPath + "\n").append("knowledgeBaseConfigPath: " + knowledgeBaseConfigPath + "\n");
      if (ruleResources != null)
      {
         buff.append("Rule Resources:\n");
         for (String rr : ruleResources)
         {
            buff.append("\t" + rr);
         }
      }
      if (eventListeners != null)
      {
         buff.append("\nEvent Listeners: \n");
         for (String el : eventListeners)
         {
            buff.append("\t" + el);
         }
      }

      return buff.toString();
   }
}
