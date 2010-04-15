/*
 * JBoss, Home of Professional Open Source
 * Copyright ${year}, Red Hat, Inc., and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */ 
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
