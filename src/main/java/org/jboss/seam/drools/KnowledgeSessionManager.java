package org.jboss.seam.drools;

import java.io.InputStream;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;

import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;
import javax.inject.Named;
import javax.security.auth.login.Configuration;

import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.event.process.ProcessEventListener;
import org.drools.event.rule.AgendaEventListener;
import org.drools.event.rule.WorkingMemoryEventListener;
import org.drools.logger.KnowledgeRuntimeLogger;
import org.drools.logger.KnowledgeRuntimeLoggerFactory;
import org.drools.runtime.KnowledgeSessionConfiguration;
import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.StatelessKnowledgeSession;
import org.drools.runtime.process.WorkItemHandler;
import org.jboss.seam.drools.events.KnowledgeSessionCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Manager component for a Drools Knowledge Sessions.
 * 
 * @author Tihomir Surdilovic
 */
public class KnowledgeSessionManager
{
   private static final Logger log = LoggerFactory.getLogger(KnowledgeSessionManager.class);

   private KnowledgeSessionManagerConfig ksessionManagerConfig;
   private Map<Integer, KnowledgeRuntimeLogger> statefulKnowledgeLoggers = new Hashtable<Integer, KnowledgeRuntimeLogger>();

   @Inject
   BeanManager manager;

   @Inject
   KnowledgeBase kbase;

   @Inject
   public KnowledgeSessionManager(KnowledgeSessionManagerConfig ksessionManagerConfig)
   {
      this.ksessionManagerConfig = ksessionManagerConfig;
   }

   @Produces
   @Named
   public StatefulKnowledgeSession getStatefulSession(InjectionPoint injectionPoint) throws Exception
   {
      StatefulKnowledgeSession ksession = kbase.newStatefulKnowledgeSession(getKSessionConfig(), null);
      addEventListeners(ksession);
      addWorkItemHandlers(ksession);
      addAuditLog(ksession);
      manager.fireEvent(new KnowledgeSessionCreatedEvent(ksession.getId()));
      return ksession;
   }

   public void disposeStatefulSession(@Disposes StatefulKnowledgeSession statefulSession)
   {
      if (statefulKnowledgeLoggers.get(statefulSession.getId()) != null)
      {
         statefulKnowledgeLoggers.get(statefulSession.getId()).close();
      }
      statefulSession.dispose();
   }

   @Produces
   @Named
   public StatelessKnowledgeSession getStatelessSession(InjectionPoint injectionPoint) throws Exception 
   {
      StatelessKnowledgeSession ksession = kbase.newStatelessKnowledgeSession(getKSessionConfig());
      addEventListeners(ksession);
      manager.fireEvent(new KnowledgeSessionCreatedEvent(-1));
      return ksession;
   }

   private KnowledgeSessionConfiguration getKSessionConfig() throws Exception
   {
      KnowledgeSessionConfiguration ksessionConfig = KnowledgeBaseFactory.newKnowledgeSessionConfiguration();
      if(ksessionManagerConfig.getKnowledgeSessionConfigProp() != null) {
         ksessionConfig = KnowledgeBaseFactory.newKnowledgeSessionConfiguration(ksessionManagerConfig.getKnowledgeSessionConfigProp());
      } else {
         // Only allow resource for .properties files
         if (ksessionManagerConfig.getKnowledgeSessionConfig() != null && ksessionManagerConfig.getKnowledgeSessionConfig().endsWith(".properties"))
         {
            Properties ksessionProp = new Properties();
            InputStream in = this.getClass().getClassLoader().getResourceAsStream(ksessionManagerConfig.getKnowledgeSessionConfig());
            if (in == null)
            {
               throw new IllegalStateException("Could not locate knowledgeSessionrConfig: " + ksessionManagerConfig.getKnowledgeSessionConfig());
            }
            ksessionProp.load(in);
            in.close();
            ksessionConfig = KnowledgeBaseFactory.newKnowledgeSessionConfiguration(ksessionProp);
            log.debug("KnowledgeSessionConfiguration loaded: " + ksessionManagerConfig.getKnowledgeSessionConfig());
         }
      }
      return ksessionConfig;
   }
   
   
   private void addAuditLog(StatefulKnowledgeSession ksession) throws Exception {
      if(ksessionManagerConfig.getAuditLog() != null) { 
         if(KnowledgeSessionManagerConfig.isFileLogger(ksessionManagerConfig.getAuditLog())) {
            String logName = KnowledgeSessionManagerConfig.getFileLoggerPath(ksessionManagerConfig.getAuditLog()) + System.currentTimeMillis(); 
            KnowledgeRuntimeLogger krLogger = KnowledgeRuntimeLoggerFactory.newFileLogger(ksession, logName);
            statefulKnowledgeLoggers.put(ksession.getId(), krLogger);
         } else if(KnowledgeSessionManagerConfig.isConsoleLogger(ksessionManagerConfig.getAuditLog())) {
            KnowledgeRuntimeLogger krLogger = KnowledgeRuntimeLoggerFactory.newConsoleLogger(ksession);
            statefulKnowledgeLoggers.put(ksession.getId(), krLogger);
         } else if(KnowledgeSessionManagerConfig.isThreadedLogger(ksessionManagerConfig.getAuditLog())) {
            String logName = KnowledgeSessionManagerConfig.getThreadedLoggerPath(ksessionManagerConfig.getAuditLog()) + System.currentTimeMillis();
            int interval = KnowledgeSessionManagerConfig.getThreadedLoggerInterval(ksessionManagerConfig.getAuditLog());
            KnowledgeRuntimeLogger krLogger = KnowledgeRuntimeLoggerFactory.newThreadedFileLogger(ksession, logName, interval);
            statefulKnowledgeLoggers.put(ksession.getId(), krLogger);
         } else {
            log.warn("Invalid logger specified: " + ksessionManagerConfig.getAuditLog());
         }
      }
   }
   
   public void addEventListeners(StatefulKnowledgeSession ksession) throws Exception {
      if(ksessionManagerConfig.getEventListeners() != null) {
         for(String eventListener : ksessionManagerConfig.getEventListeners()) {
            Class eventListenerClass = Class.forName(eventListener);
            Object eventListenerObject = eventListenerClass.newInstance();
           
            if(eventListenerObject instanceof WorkingMemoryEventListener) {
               ksession.addEventListener((WorkingMemoryEventListener) eventListenerObject);
            } else if(eventListenerObject instanceof AgendaEventListener) {
               ksession.addEventListener((AgendaEventListener) eventListenerObject);
            } else if(eventListenerObject instanceof ProcessEventListener) {
               ksession.addEventListener((ProcessEventListener) eventListenerObject);
            } else {
               log.debug("Invalid Event Listener: " + eventListener);
            }
         }
      }
   }
   
   public void addEventListeners(StatelessKnowledgeSession ksession) throws Exception{
      if(ksessionManagerConfig.getEventListeners() != null) {
         for(String eventListener : ksessionManagerConfig.getEventListeners()) {
            Class eventListenerClass = Class.forName(eventListener);
            Object eventListenerObject = eventListenerClass.newInstance();
           
            if(eventListenerObject instanceof WorkingMemoryEventListener) {
               ksession.addEventListener((WorkingMemoryEventListener) eventListenerObject);
            } else if(eventListenerObject instanceof AgendaEventListener) {
               ksession.addEventListener((AgendaEventListener) eventListenerObject);
            } else if(eventListenerObject instanceof ProcessEventListener) {
               ksession.addEventListener((ProcessEventListener) eventListenerObject);
            } else {
               log.debug("Invalid Event Listener: " + eventListener);
            }
         }
      }
   }
   
   public void addWorkItemHandlers(StatefulKnowledgeSession ksession) {
      if(ksessionManagerConfig.getWorkItemHandlers() != null) {
         for(String workItemHandlerStr : ksessionManagerConfig.getWorkItemHandlers()) {
            if(KnowledgeSessionManagerConfig.isValidWorkItemHandler(workItemHandlerStr)) {                              
               @SuppressWarnings("unchecked")
               Bean<WorkItemHandler> workItemHandlerBean = (Bean<WorkItemHandler>) manager.getBeans(KnowledgeSessionManagerConfig.getWorkItemHandlerType(workItemHandlerStr)).iterator().next();
               WorkItemHandler handler = (WorkItemHandler) manager.getReference(workItemHandlerBean, Configuration.class, manager.createCreationalContext(workItemHandlerBean));
               log.debug("Registering new WorkItemHandler: " + KnowledgeSessionManagerConfig.getWorkItemHandlerName(workItemHandlerStr));
               ksession.getWorkItemManager().registerWorkItemHandler(KnowledgeSessionManagerConfig.getWorkItemHandlerName(workItemHandlerStr), handler);
            }
         }
      }
   }
   

}
