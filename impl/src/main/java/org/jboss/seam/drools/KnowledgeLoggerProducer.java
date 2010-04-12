package org.jboss.seam.drools;

import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.Produces;

import org.drools.event.KnowledgeRuntimeEventManager;
import org.drools.logger.KnowledgeRuntimeLogger;
import org.drools.logger.KnowledgeRuntimeLoggerFactory;
import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.StatelessKnowledgeSession;
import org.jboss.seam.drools.config.DroolsConfiguration;
import org.jboss.seam.drools.qualifiers.KAgentConfigured;
import org.jboss.seam.drools.qualifiers.KBaseConfigured;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author Tihomir Surdilovic
 */
public class KnowledgeLoggerProducer
{
   private static final Logger log = LoggerFactory.getLogger(KnowledgeLoggerProducer.class);

   @Produces
   @KBaseConfigured
   public KnowledgeRuntimeLogger produceStatefulKnowledgeLogger(@KBaseConfigured StatefulKnowledgeSession ksession, Instance<DroolsConfiguration> loggerConfigInstance)
   {
      return getLogger(ksession, loggerConfigInstance.get());
   }
   
   @Produces
   @KAgentConfigured
   public KnowledgeRuntimeLogger produceStatefulKnowledgeLoggerForKAgent(@KAgentConfigured StatefulKnowledgeSession ksession, Instance<DroolsConfiguration> loggerConfigInstance)
   {
      return getLogger(ksession, loggerConfigInstance.get());
   }

   @Produces
   @KBaseConfigured
   public KnowledgeRuntimeLogger produceStatelessKnowledgeLogger(@KBaseConfigured StatelessKnowledgeSession ksession, Instance<DroolsConfiguration> loggerConfigInstance)
   {
      return getLogger(ksession, loggerConfigInstance.get());
   }
   
   @Produces
   @KAgentConfigured
   public KnowledgeRuntimeLogger produceStatelessKnowledgeLoggerForKAgent(@KAgentConfigured StatelessKnowledgeSession ksession, Instance<DroolsConfiguration> loggerConfigInstance)
   {
      return getLogger(ksession, loggerConfigInstance.get());
   }

   private KnowledgeRuntimeLogger getLogger(KnowledgeRuntimeEventManager ksession, DroolsConfiguration loggerConfig)
   {
      KnowledgeRuntimeLogger krLogger = null;
      if (loggerConfig.getLoggerType().equalsIgnoreCase("file"))
      {
         String logName = loggerConfig.getLoggerPath() + System.currentTimeMillis();
         krLogger = KnowledgeRuntimeLoggerFactory.newFileLogger(ksession, logName);
      }
      else if (loggerConfig.getLoggerType().equalsIgnoreCase("console"))
      {
         krLogger = KnowledgeRuntimeLoggerFactory.newConsoleLogger(ksession);
      }
      else if (loggerConfig.getLoggerType().equalsIgnoreCase("threaded"))
      {
         String logName = loggerConfig.getLoggerPath() + System.currentTimeMillis();
         krLogger = KnowledgeRuntimeLoggerFactory.newThreadedFileLogger(ksession, logName, loggerConfig.getLoggerInterval());
      }
      else
      {
         log.error("Invalid logger specified: type: " + loggerConfig.getLoggerType() + " path: " + loggerConfig.getLoggerPath() + " interval: " + loggerConfig.getLoggerInterval());
      }
      return krLogger;
   }

   public void disposeKBaseConfiguredKnowledgeLogger(@Disposes @KBaseConfigured KnowledgeRuntimeLogger logger)
   {
      logger.close();
   }
   
   public void disposeKAgentConfiguredKnowledgeLogger(@Disposes @KAgentConfigured KnowledgeRuntimeLogger logger) {
      logger.close();
   }
}
