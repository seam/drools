package org.jboss.seam.drools.config;

/**
 * 
 * @author Tihomir Surdilovic
 */
public class KnowledgeBaseConfig
{
   private String knowledgeBuilderConfig;
   private String knowledgeBaseConfig;
   private String[] ruleResources;
   private String[] eventListeners;

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

   public String toString()
   {
      StringBuffer buff = new StringBuffer();
      buff.append("knowledgeBuilderConfig: " + knowledgeBuilderConfig + "\n").append("knowledgeBaseConfig: " + knowledgeBaseConfig + "\n");
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
