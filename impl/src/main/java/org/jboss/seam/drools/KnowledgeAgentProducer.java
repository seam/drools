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
import java.util.Iterator;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;

import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.agent.KnowledgeAgent;
import org.drools.agent.KnowledgeAgentFactory;
import org.drools.io.ResourceFactory;
import org.jboss.seam.drools.config.Drools;
import org.jboss.seam.drools.config.RuleResource;
import org.jboss.seam.drools.config.RuleResources;
import org.jboss.seam.drools.configutil.DroolsConfigUtil;
import org.jboss.seam.drools.qualifiers.Scanned;
import org.jboss.weld.extensions.bean.generic.Generic;
import org.jboss.weld.extensions.bean.generic.GenericProduct;
import org.jboss.weld.extensions.core.Veto;
import org.jboss.weld.extensions.resourceLoader.ResourceProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * KnowledgeAgent producer.
 * 
 * @author Tihomir Surdilovic
 */
@Veto
@Dependent
@Generic(Drools.class)
public class KnowledgeAgentProducer implements Serializable
{
   private static final Logger log = LoggerFactory.getLogger(KnowledgeAgentProducer.class);

   @Inject
   BeanManager manager;
   
   @Inject
   ResourceProvider resourceProvider;

   @Inject
   Drools config;
   
   @Inject
   @GenericProduct
   DroolsConfigUtil configUtils;
   
   @Inject 
   @GenericProduct
   RuleResources ruleResources;

   @Produces
   @ApplicationScoped
   public KnowledgeAgent produceKnowledgeAgent() throws Exception
   {
      return getAgent();
   }
   
   @Produces
   @Scanned
   @ApplicationScoped
   public KnowledgeBase produceScannedKnowledgeBase() throws Exception
   {
      KnowledgeAgent agent = getAgent();
      return agent.getKnowledgeBase();
   }
   
   private KnowledgeAgent getAgent() throws Exception
   {
      ResourceFactory.getResourceChangeScannerService().configure(configUtils.getResourceChangeScannerConfiguration());

      KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase(configUtils.getKnowledgeBaseConfiguration());
      KnowledgeAgent kagent = KnowledgeAgentFactory.newKnowledgeAgent(config.agentName(), kbase, configUtils.getKnowledgeAgentConfiguration());

      Iterator<RuleResource> resourceIterator = ruleResources.iterator();
      while(resourceIterator.hasNext()) {
         kagent.applyChangeSet(resourceIterator.next().getDroolsResouce());
      }
      
      if (config.startChangeNotifierService())
      {
         ResourceFactory.getResourceChangeNotifierService().start();
      }
      if (config.startChangeScannerService())
      {
         ResourceFactory.getResourceChangeScannerService().start();
      }
      return kagent;

   }

   public void disposeScannedKnowledgeBase(/* @Disposes */@Scanned KnowledgeBase kbase)
   {
      // do we really want to stop ?
      ResourceFactory.getResourceChangeNotifierService().stop();
      ResourceFactory.getResourceChangeScannerService().stop();
   }
}
