package org.jboss.seam.drools;

import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;

import org.drools.event.KnowledgeRuntimeEventManager;
import org.drools.logger.KnowledgeRuntimeLogger;
import org.drools.logger.KnowledgeRuntimeLoggerFactory;
import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.StatelessKnowledgeSession;
import org.jboss.seam.drools.config.DroolsConfiguration;
import org.jboss.seam.drools.qualifiers.Scanned;
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
   public KnowledgeRuntimeLogger produceStatefulKnowledgeLogger(StatefulKnowledgeSession ksession, DroolsConfiguration loggerConfig)
   {
      return getLogger(ksession, loggerConfig);
   }
   
   @Produces
   @Scanned
   public KnowledgeRuntimeLogger produceScannedStatefulKnowledgeLogger(@Scanned StatefulKnowledgeSession ksession, DroolsConfiguration loggerConfig)
   {
      return getLogger(ksession, loggerConfig);
   }

   @Produces
   public KnowledgeRuntimeLogger produceStatelessKnowledgeLogger(StatelessKnowledgeSession ksession, DroolsConfiguration loggerConfig)
   {
      return getLogger(ksession, loggerConfig);
   }
   
   @Produces
   @Scanned
   public KnowledgeRuntimeLogger produceStatelessKnowledgeLoggerForKAgent(@Scanned StatelessKnowledgeSession ksession, DroolsConfiguration loggerConfig)
   {
      return getLogger(ksession, loggerConfig);
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

   public void disposeKnowledgeRuntimeLogger(@Disposes KnowledgeRuntimeLogger logger)
   {
      logger.close();
   }
   
   public void disposeScannedKnowledgeRuntimeLogger(@Disposes @Scanned KnowledgeRuntimeLogger logger) {
      logger.close();
   }
}
