package org.jboss.seam.drools;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;

import javax.annotation.PreDestroy;
import javax.enterprise.context.Dependent;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;
import javax.inject.Named;

import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.logger.KnowledgeRuntimeLogger;
import org.drools.runtime.KnowledgeSessionConfiguration;
import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.StatelessKnowledgeSession;
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
   private KnowledgeRuntimeLogger statelessKLogger;
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
      //addEventListeners(ksession);
      //addWorkItemHandlers(ksession);
      //addAuditLog(ksession);
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
      //addEventListeners(ksession);
      //addAuditLog(ksession);
      manager.fireEvent(new KnowledgeSessionCreatedEvent(-1));
      return ksession;
   }

   public void disposeStatelessSession(@Disposes StatelessKnowledgeSession statelessSession)
   {
      if (statelessKLogger != null)
      {
         statelessKLogger.close();
      }
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

}
