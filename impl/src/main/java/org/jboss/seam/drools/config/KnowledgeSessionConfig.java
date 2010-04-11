package org.jboss.seam.drools.config;

/**
 * 
 * @author Tihomir Surdilovic
 */
public class KnowledgeSessionConfig
{
   private String[] eventListeners;
   private String[] workItemHandlers;
   private String knowledgeSessionConfigPath;

   public String[] getEventListeners()
   {
      return eventListeners;
   }

   public void setEventListeners(String[] eventListeners)
   {
      this.eventListeners = eventListeners;
   }

   public String[] getWorkItemHandlers()
   {
      return workItemHandlers;
   }

   public void setWorkItemHandlers(String[] workItemHandlers)
   {
      this.workItemHandlers = workItemHandlers;
   }

   public String getKnowledgeSessionConfigPath()
   {
      return knowledgeSessionConfigPath;
   }

   public void setKnowledgeSessionConfigPath(String knowledgeSessionConfigPath)
   {
      this.knowledgeSessionConfigPath = knowledgeSessionConfigPath;
   }

   public String toString()
   {
      StringBuffer buff = new StringBuffer();
      buff.append("knowledgeSessionConfigPath: " + knowledgeSessionConfigPath + "\n");
      if (eventListeners != null)
      {
         buff.append("Event Listeners:\n");
         for (String el : eventListeners)
         {
            buff.append("\t" + el);
         }
      }
      if (workItemHandlers != null)
      {
         buff.append("\nWorkitem handlers: \n");
         for (String el : workItemHandlers)
         {
            buff.append("\t" + el);
         }
      }
      return buff.toString();
   }
}
