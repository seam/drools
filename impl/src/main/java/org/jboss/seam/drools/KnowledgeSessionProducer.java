package org.jboss.seam.drools;

import java.io.InputStream;
import java.util.Properties;

import javax.enterprise.inject.Any;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;

import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.event.KnowledgeRuntimeEventManager;
import org.drools.event.process.ProcessEventListener;
import org.drools.event.rule.AgendaEventListener;
import org.drools.event.rule.WorkingMemoryEventListener;
import org.drools.runtime.KnowledgeSessionConfiguration;
import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.StatelessKnowledgeSession;
import org.drools.runtime.process.WorkItemHandler;
import org.jboss.seam.drools.config.KnowledgeSessionConfig;
import org.jboss.seam.drools.utils.ConfigUtils;
import org.jboss.weld.extensions.resources.ResourceProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author Tihomir Surdilovic
 */
public class KnowledgeSessionProducer
{
   private static final Logger log = LoggerFactory.getLogger(KnowledgeSessionProducer.class);
   
   @Inject BeanManager manager;
   @Inject ResourceProvider resourceProvider;
   
   @Produces public StatefulKnowledgeSession produceStatefulSession(@Any KnowledgeBase kbase, KnowledgeSessionConfig ksessionConfig) throws Exception {
      StatefulKnowledgeSession ksession = kbase.newStatefulKnowledgeSession(getConfig(ksessionConfig), null);
      addEventListeners(ksession, ksessionConfig);
      addWorkItemHandlers(ksession, ksessionConfig);
      
      return ksession;
   }
   
   @Produces public StatelessKnowledgeSession produceStatelessSession(@Any KnowledgeBase kbase, KnowledgeSessionConfig ksessionConfig) throws Exception {
      StatelessKnowledgeSession ksession = kbase.newStatelessKnowledgeSession(getConfig(ksessionConfig));
      addEventListeners(ksession, ksessionConfig);
      
      return ksession;
   }
   
   void disposeStatefulSession(@Disposes StatefulKnowledgeSession session) {
      session.dispose();
   }
   
   void disposeStatelessSession(@Disposes StatelessKnowledgeSession session) {
      session = null;
   }
   
   private KnowledgeSessionConfiguration getConfig(KnowledgeSessionConfig ksessionConfig) throws Exception{
      KnowledgeSessionConfiguration droolsKsessionConfig = KnowledgeBaseFactory.newKnowledgeSessionConfiguration();
      if (ksessionConfig.getKnowledgeSessionConfig() != null && ksessionConfig.getKnowledgeSessionConfig().endsWith(".properties"))
      {
         Properties ksessionProp = new Properties();
         //InputStream in = this.getClass().getClassLoader().getResourceAsStream(ksessionManagerConfig.getKnowledgeSessionConfig());
         InputStream in = resourceProvider.loadResourceStream(ksessionConfig.getKnowledgeSessionConfig()); 
         if (in == null)
         {
            throw new IllegalStateException("Could not locate knowledgeSessionrConfig: " + ksessionConfig.getKnowledgeSessionConfig());
         }
         ksessionProp.load(in);
         in.close();
         droolsKsessionConfig = KnowledgeBaseFactory.newKnowledgeSessionConfiguration(ksessionProp);
         log.debug("KnowledgeSessionConfiguration loaded: " + ksessionConfig.getKnowledgeSessionConfig());
      } else {
         
      }
      return droolsKsessionConfig;
   }
   
   private void addEventListeners(KnowledgeRuntimeEventManager ksession, KnowledgeSessionConfig ksessionConfig) throws Exception {
      if(ksessionConfig.getEventListeners() != null) {
         for(String eventListener : ksessionConfig.getEventListeners()) {
            @SuppressWarnings("unchecked")
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
   
   private void addWorkItemHandlers(StatefulKnowledgeSession ksession, KnowledgeSessionConfig ksessionConfig) {
      if(ksessionConfig.getWorkItemHandlers() != null) {
         for(String workItemHandlerStr : ksessionConfig.getWorkItemHandlers()) {
            if(ConfigUtils.isValidWorkItemHandler(workItemHandlerStr)) {                              
               @SuppressWarnings("unchecked")
               Bean<WorkItemHandler> workItemHandlerBean = (Bean<WorkItemHandler>) manager.getBeans(ConfigUtils.getWorkItemHandlerType(workItemHandlerStr)).iterator().next();
               WorkItemHandler handler = (WorkItemHandler) manager.getReference(workItemHandlerBean, WorkItemHandler.class, manager.createCreationalContext(workItemHandlerBean));
               log.debug("Registering new WorkItemHandler: " + ConfigUtils.getWorkItemHandlerName(workItemHandlerStr));
               ksession.getWorkItemManager().registerWorkItemHandler(ConfigUtils.getWorkItemHandlerName(workItemHandlerStr), handler);
            } else {
               log.warn("Invalid workitem handler configuration for: " + workItemHandlerStr);
            }
         }
      }
   }
}
