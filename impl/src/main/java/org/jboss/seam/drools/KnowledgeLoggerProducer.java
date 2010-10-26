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

import java.io.Serializable;

import javax.enterprise.context.Dependent;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import org.drools.event.KnowledgeRuntimeEventManager;
import org.drools.logger.KnowledgeRuntimeLogger;
import org.drools.logger.KnowledgeRuntimeLoggerFactory;
import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.StatelessKnowledgeSession;
import org.jboss.seam.drools.config.Drools;
import org.jboss.seam.drools.config.DroolsConfig;
import org.jboss.seam.drools.configutil.DroolsConfigUtil;
import org.jboss.seam.drools.qualifiers.Scanned;
import org.jboss.weld.extensions.bean.generic.Generic;
import org.jboss.weld.extensions.bean.generic.GenericConfiguration;
import org.jboss.weld.extensions.core.Veto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Producer for stateful and stateless KnowledgerRuntimeLogger.
 * 
 * @author Tihomir Surdilovic
 */
@Veto 
@Dependent
@GenericConfiguration(Drools.class)
public class KnowledgeLoggerProducer implements Serializable
{
   private static final long serialVersionUID = 1537722646752938903L;

   private static final Logger log = LoggerFactory.getLogger(KnowledgeLoggerProducer.class);

   @Inject DroolsConfig config;

   @Inject @Generic DroolsConfigUtil configUtils;

   @Inject @Generic StatefulKnowledgeSession statefullKsession;

   @Inject @Scanned @Generic StatefulKnowledgeSession scannedStatefullKsession;

   @Inject @Generic StatelessKnowledgeSession statelessKsession;

   @Inject @Scanned @Generic
   StatelessKnowledgeSession scannedStatelessKsession;

   @RequestScoped
   public KnowledgeRuntimeLogger produceStatefulKnowledgeLogger()
   {
      return getLogger(statefullKsession);
   }

   @Scanned
   @RequestScoped
   public KnowledgeRuntimeLogger produceScannedStatefulKnowledgeLogger()
   {
      return getLogger(scannedStatefullKsession);
   }


   @RequestScoped
   public KnowledgeRuntimeLogger produceStatelessKnowledgeLogger()
   {
      return getLogger(statelessKsession);
   }


   @Scanned
   @RequestScoped
   public KnowledgeRuntimeLogger produceScannedStatelessKnowledgeLogger()
   {
      return getLogger(scannedStatelessKsession);
   }

   private KnowledgeRuntimeLogger getLogger(KnowledgeRuntimeEventManager ksession)
   {
      KnowledgeRuntimeLogger krLogger = null;
      if (config.getLoggerType() != null && config.getLoggerType().equalsIgnoreCase("file"))
      {
         if (config.getLoggerPath() == null || config.getLoggerName() == null)
         {
            log.error("Invalid file logger information - path: " + 
                  config.getLoggerPath() + ", name: " + config.getLoggerName());
         }
         else
         {
            String logName = config.getLoggerPath() + config.getLoggerName() + System.currentTimeMillis();
            krLogger = KnowledgeRuntimeLoggerFactory.newFileLogger(ksession, logName);
         }
      }
      else if (config.getLoggerType() != null && config.getLoggerType().equalsIgnoreCase("console"))
      {
         krLogger = KnowledgeRuntimeLoggerFactory.newConsoleLogger(ksession);
      }
      else if (config.getLoggerType() != null && config.getLoggerType().equalsIgnoreCase("threaded"))
      {
         if (config.getLoggerPath() == null || config.getLoggerName() == null || 
               config.getLoggerInterval() == -1)
         {
            log.error("Invalid threaded logger information - path: " + 
                  config.getLoggerPath() + ", name: " + config.getLoggerName() + 
                  ", interval: " + config.getLoggerInterval());
         }
         else
         {
            String logName = config.getLoggerPath() + config.getLoggerName() + 
                System.currentTimeMillis();
            krLogger = KnowledgeRuntimeLoggerFactory.newThreadedFileLogger(
                  ksession, logName, config.getLoggerInterval());
         }
      }
      else
      {
         log.error("Invalid logger specified: type: " + config.getLoggerType() + 
               " path: " + config.getLoggerPath() + " interval: " + config.getLoggerInterval());
      }
      return krLogger;
   }

   public void disposeKnowledgeRuntimeLogger(/* @Disposes */KnowledgeRuntimeLogger logger)
   {
      logger.close();
   }

   public void disposeScannedKnowledgeRuntimeLogger(/* @Disposes */@Scanned KnowledgeRuntimeLogger logger)
   {
      logger.close();
   }
}
