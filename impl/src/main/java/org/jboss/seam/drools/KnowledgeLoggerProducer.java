package org.jboss.seam.drools;

import java.util.Map;

import java.lang.annotation.Annotation;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;

import org.drools.event.KnowledgeRuntimeEventManager;
import org.drools.logger.KnowledgeRuntimeLogger;
import org.drools.logger.KnowledgeRuntimeLoggerFactory;
import org.drools.runtime.StatefulKnowledgeSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author Tihomir Surdilovic
 */
public class KnowledgeLoggerProducer
{
   private static final String fileLoggerType = "file";
   private static final String consoleLoggerType = "console";
   private static final String threadedLoggerType = "threaded";
   private static final String loggerType = "type";
   private static final String loggerPath = "path";
   private static final String loggerInterval = "interval";
   
   private static final Logger log = LoggerFactory.getLogger(KnowledgeLoggerProducer.class);
   
   
   @Produces public KnowledgeRuntimeLogger produceKnowledgeLogger(InjectionPoint ip, Instance<KnowledgeRuntimeEventManager> ksessionInstance, Instance<Map<String, String>> loggerInfoInstance) {
      KnowledgeRuntimeLogger krLogger = null;
      KnowledgeRuntimeEventManager ksession = ksessionInstance.select(ip.getQualifiers().toArray(new Annotation[0])).get();
      Map<String, String> loggerInfo = loggerInfoInstance.select(ip.getQualifiers().toArray(new Annotation[0])).get(); 
      if(loggerInfo.get(loggerType).equalsIgnoreCase(fileLoggerType)) {
         String logName = loggerInfo.get(loggerPath) + System.currentTimeMillis(); 
         krLogger = KnowledgeRuntimeLoggerFactory.newFileLogger(ksession, logName);
      } else if(loggerInfo.get(loggerType).equalsIgnoreCase(consoleLoggerType)) {
         krLogger = KnowledgeRuntimeLoggerFactory.newConsoleLogger(ksession);
      } else if(loggerInfo.get(loggerType).equalsIgnoreCase(threadedLoggerType)) {
         String logName = loggerInfo.get(loggerPath) + System.currentTimeMillis();
         krLogger = KnowledgeRuntimeLoggerFactory.newThreadedFileLogger(ksession, logName, Integer.parseInt(loggerInfo.get(loggerInterval)));
      } else {
         log.error("Invalid logger specified: type: " + loggerInfo.get(loggerType) + " path: " + loggerInfo.get(loggerPath) + " loggerInfo.get(loggerInterval): " + loggerInfo.get(loggerInterval));
      }
      return krLogger;
   }
   
   public void disposeKnowledgeLogger(@Disposes KnowledgeRuntimeLogger logger) {
      logger.close();
   }
}
