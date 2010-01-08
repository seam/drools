package org.jboss.seam.drools;

import java.util.Properties;
import java.util.regex.Pattern;

/**
 * KnowledgeSessionManager Configuration.
 * 
 * @author Tihomir Surdilovic
 */
public class KnowledgeSessionManagerConfig
{ 
   private static final Pattern DIVIDER = Pattern.compile(";");
   private static final int WORKITEMHANDLER_NAME = 0;
   private static final int WORKITEMHANDLER_TYPE = 1;
   
   private String[] eventListeners;
   private String[] workItemHandlers;
   private String knowledgeSessionConfig;
   private Properties knowledgeSessionConfigProp;
   private String auditLog;
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
   public String getKnowledgeSessionConfig()
   {
      return knowledgeSessionConfig;
   }
   public void setKnowledgeSessionConfig(String knowledgeSessionConfig)
   {
      this.knowledgeSessionConfig = knowledgeSessionConfig;
   }
   public String getAuditLog()
   {
      return auditLog;
   }
   public void setAuditLog(String auditLog)
   {
      this.auditLog = auditLog;
   }
   public Properties getKnowledgeSessionConfigProp()
   {
      return knowledgeSessionConfigProp;
   }
   public void setKnowledgeSessionConfigProp(Properties knowledgeSessionConfigProp)
   {
      this.knowledgeSessionConfigProp = knowledgeSessionConfigProp;
   }

}
