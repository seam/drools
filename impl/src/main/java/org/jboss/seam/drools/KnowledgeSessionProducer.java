package org.jboss.seam.drools;

import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.util.AnnotationLiteral;
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
import org.jboss.seam.drools.config.DroolsConfiguration;
import org.jboss.seam.drools.qualifiers.KAgentConfigured;
import org.jboss.seam.drools.qualifiers.KBaseConfigured;
import org.jboss.seam.drools.qualifiers.KSessionEventListener;
import org.jboss.seam.drools.qualifiers.WIHandler;
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

   @Inject
   BeanManager manager;
   @Inject
   ResourceProvider resourceProvider;

   @Produces
   @KBaseConfigured
   public StatefulKnowledgeSession produceStatefulSession(@KBaseConfigured KnowledgeBase kbase, Instance<DroolsConfiguration> ksessionConfigInstance) throws Exception
   {
      StatefulKnowledgeSession ksession = kbase.newStatefulKnowledgeSession(getConfig(ksessionConfigInstance.get().getKnowledgeSessionConfigPath()), null);
      addEventListeners(ksession);
      addWorkItemHandlers(ksession);

      return ksession;
   }

   @Produces
   @KAgentConfigured
   StatefulKnowledgeSession produceStatefulSessionFromKAgent(@KAgentConfigured KnowledgeBase kbase, Instance<DroolsConfiguration> ksessionConfigInstance) throws Exception
   {
      StatefulKnowledgeSession ksession = kbase.newStatefulKnowledgeSession(getConfig(ksessionConfigInstance.get().getKnowledgeSessionConfigPath()), null);
      addEventListeners(ksession);
      addWorkItemHandlers(ksession);

      return ksession;
   }

   @Produces
   @KAgentConfigured
   StatelessKnowledgeSession produceStatelessSessionFromKAgent(@KAgentConfigured KnowledgeBase kbase, Instance<DroolsConfiguration> ksessionConfigInstance) throws Exception
   {
      StatelessKnowledgeSession ksession = kbase.newStatelessKnowledgeSession(getConfig(ksessionConfigInstance.get().getKnowledgeSessionConfigPath()));
      addEventListeners(ksession);

      return ksession;
   }

   @Produces
   @KBaseConfigured
   public StatelessKnowledgeSession produceStatelessSession(@KBaseConfigured KnowledgeBase kbase, Instance<DroolsConfiguration> ksessionConfigInstance) throws Exception
   {
      StatelessKnowledgeSession ksession = kbase.newStatelessKnowledgeSession(getConfig(ksessionConfigInstance.get().getKnowledgeSessionConfigPath()));
      addEventListeners(ksession);

      return ksession;
   }

   void disposeStatefulSession(@Disposes @Any StatefulKnowledgeSession session)
   {
      session.dispose();
   }

   private KnowledgeSessionConfiguration getConfig(String knowledgeSessionConfigPath) throws Exception
   {
      KnowledgeSessionConfiguration droolsKsessionConfig = KnowledgeBaseFactory.newKnowledgeSessionConfiguration();
      if (knowledgeSessionConfigPath != null && knowledgeSessionConfigPath.endsWith(".properties"))
      {
         Properties ksessionProp = ConfigUtils.loadProperties(resourceProvider, knowledgeSessionConfigPath);
         droolsKsessionConfig = KnowledgeBaseFactory.newKnowledgeSessionConfiguration(ksessionProp);
         log.debug("KnowledgeSessionConfiguration loaded: " + knowledgeSessionConfigPath);
      }
      else
      {
         log.warn("Invalid config type: " + knowledgeSessionConfigPath);
      }
      return droolsKsessionConfig;
   }

   private void addEventListeners(KnowledgeRuntimeEventManager ksession)
   {
      Set<Bean<?>> allKSessionEventListeners = manager.getBeans(Object.class, new AnnotationLiteral<KSessionEventListener>()
      {
      });
      if(allKSessionEventListeners != null) {
         Iterator<Bean<?>> iter = allKSessionEventListeners.iterator();
         while(iter.hasNext()) {
            Bean<?> eventListener = iter.next();
            CreationalContext<?> context = manager.createCreationalContext(eventListener);
            Object eventListenerInstance = manager.getReference(eventListener, Object.class, context);
            
            if (eventListenerInstance instanceof WorkingMemoryEventListener)
            {
               ksession.addEventListener((WorkingMemoryEventListener) eventListenerInstance);
            }
            else if (eventListenerInstance instanceof AgendaEventListener)
            {
               ksession.addEventListener((AgendaEventListener) eventListenerInstance);
            }
            else if (eventListenerInstance instanceof ProcessEventListener)
            {
               ksession.addEventListener((ProcessEventListener) eventListenerInstance);
            }
            else
            {
               log.debug("Invalid Event Listener: " + eventListenerInstance);
            }
         }
      }
   }

   private void addWorkItemHandlers(StatefulKnowledgeSession ksession)
   {
      Set<Bean<?>> allWorkItemHandlers = manager.getBeans(WorkItemHandler.class, new AnnotationLiteral<WIHandler>()
      {
      });
      if (allWorkItemHandlers != null)
      {
         Iterator<Bean<?>> iter = allWorkItemHandlers.iterator();
         while (iter.hasNext())
         {
            Bean<?> handler = iter.next();
            WIHandler handlerQualifier = (WIHandler) handler.getQualifiers().toArray()[0];
            CreationalContext<?> context = manager.createCreationalContext(handler);
            WorkItemHandler handlerInstance = (WorkItemHandler) manager.getReference(handler, WorkItemHandler.class, context);
            
            log.info("Registering new WorkItemHandler: " + handlerQualifier.name());
            ksession.getWorkItemManager().registerWorkItemHandler(handlerQualifier.name(), handlerInstance);
         }
      }
   }
}
