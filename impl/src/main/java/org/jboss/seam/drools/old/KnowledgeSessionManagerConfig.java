package org.jboss.seam.drools.old;

import java.util.Properties;
import java.util.regex.Pattern;

/**
 * KnowledgeSessionManager Configuration.
 * 
 * @author Tihomir Surdilovic
 */
public class KnowledgeSessionManagerConfig
{ 
  /** private static final Pattern DIVIDER = Pattern.compile(";");
   private static final int WORKITEMHANDLER_NAME = 0;
   private static final int WORKITEMHANDLER_TYPE = 1;
   private static final int AUDIT_LOG_TYPE = 0;
   private static final int AUDIT_LOG_INFO = 1;
   private static final int AUDIT_LOG_INTERVAL = 2;
   private static final String AUDIT_LOG_TYPE_FILE = "file";
   private static final String AUDIT_LOG_TYPE_CONSOLE = "console";
   private static final String AUDIT_LOG_TYPE_THREADED = "threaded";
   
   private String[] eventListeners;
   private String[] workItemHandlers;
   private String knowledgeSessionConfig;
   private Properties knowledgeSessionConfigProp;
   private String auditLog;
   
   public static String getWorkItemHandlerName(String workItemHandlerStr) {
      return DIVIDER.split(workItemHandlerStr.trim())[WORKITEMHANDLER_NAME];
   }
   
   public static String getWorkItemHandlerType(String workItemHandlerStr) {
      return DIVIDER.split(workItemHandlerStr.trim())[WORKITEMHANDLER_TYPE];
   }
   
   public static boolean isValidWorkItemHandler(String workItemHandlerStr) {
      return DIVIDER.split(workItemHandlerStr.trim()).length == 2;
   }
   
   public static boolean isFileLogger(String auditLog) {
      return DIVIDER.split(auditLog.trim())[AUDIT_LOG_TYPE] == AUDIT_LOG_TYPE_FILE;
   }
   
   public static boolean isConsoleLogger(String auditLog) {
      return DIVIDER.split(auditLog.trim())[AUDIT_LOG_TYPE] == AUDIT_LOG_TYPE_CONSOLE;
   }
   
   public static boolean isThreadedLogger(String auditLog) {
      return DIVIDER.split(auditLog.trim())[AUDIT_LOG_TYPE] == AUDIT_LOG_TYPE_THREADED;
   }
   
   public static String getFileLoggerPath(String auditLog) {
      return DIVIDER.split(auditLog.trim())[AUDIT_LOG_INFO];
   }
   
   public static String getThreadedLoggerPath(String auditLog) {
      return DIVIDER.split(auditLog.trim())[AUDIT_LOG_INFO];
   }
   
   public static int getThreadedLoggerInterval(String auditLog) {
      return Integer.parseInt(DIVIDER.split(auditLog.trim())[AUDIT_LOG_INTERVAL]);
   }
   
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

**/}
