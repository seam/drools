package org.jboss.seam.drools;

import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;

import org.drools.event.KnowledgeRuntimeEventManager;
import org.drools.logger.KnowledgeRuntimeLogger;
import org.drools.logger.KnowledgeRuntimeLoggerFactory;
import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.StatelessKnowledgeSession;
import org.jboss.seam.drools.config.KnowledgeLoggerConfig;
import org.jboss.seam.drools.qualifiers.kbase.KAgentConfigured;
import org.jboss.seam.drools.qualifiers.kbase.KBaseConfigured;
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
   public KnowledgeRuntimeLogger produceStatefulKnowledgeLogger(@KBaseConfigured StatefulKnowledgeSession ksession, KnowledgeLoggerConfig loggerConfig)
   {
      return getLogger(ksession, loggerConfig);
   }
   
   @Produces
   public KnowledgeRuntimeLogger produceStatefulKnowledgeLoggerForKAgent(@KAgentConfigured StatefulKnowledgeSession ksession, KnowledgeLoggerConfig loggerConfig)
   {
      return getLogger(ksession, loggerConfig);
   }

   @Produces
   public KnowledgeRuntimeLogger produceStatelessKnowledgeLogger(@KBaseConfigured StatelessKnowledgeSession ksession, KnowledgeLoggerConfig loggerConfig)
   {
      return getLogger(ksession, loggerConfig);
   }
   
   @Produces
   public KnowledgeRuntimeLogger produceStatelessKnowledgeLoggerForKAgent(@KAgentConfigured StatelessKnowledgeSession ksession, KnowledgeLoggerConfig loggerConfig)
   {
      return getLogger(ksession, loggerConfig);
   }

   private KnowledgeRuntimeLogger getLogger(KnowledgeRuntimeEventManager ksession, KnowledgeLoggerConfig loggerConfig)
   {
      KnowledgeRuntimeLogger krLogger = null;
      if (loggerConfig.isFileType())
      {
         String logName = loggerConfig.getPath() + System.currentTimeMillis();
         krLogger = KnowledgeRuntimeLoggerFactory.newFileLogger(ksession, logName);
      }
      else if (loggerConfig.isConsoleType())
      {
         krLogger = KnowledgeRuntimeLoggerFactory.newConsoleLogger(ksession);
      }
      else if (loggerConfig.isThreadedType())
      {
         String logName = loggerConfig.getPath() + System.currentTimeMillis();
         krLogger = KnowledgeRuntimeLoggerFactory.newThreadedFileLogger(ksession, logName, loggerConfig.getInterval());
      }
      else
      {
         log.error("Invalid logger specified: type: " + loggerConfig.getType() + " path: " + loggerConfig.getPath() + " interval: " + loggerConfig.getInterval());
      }
      return krLogger;
   }

   public void disposeKnowledgeLogger(@Disposes KnowledgeRuntimeLogger logger)
   {
      logger.close();
   }
}
