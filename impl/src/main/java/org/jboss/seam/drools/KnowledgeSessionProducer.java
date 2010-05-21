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
import java.util.Map.Entry;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.New;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;

import org.drools.KnowledgeBase;
import org.drools.event.KnowledgeRuntimeEventManager;
import org.drools.event.process.ProcessEventListener;
import org.drools.event.rule.AgendaEventListener;
import org.drools.event.rule.WorkingMemoryEventListener;
import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.StatelessKnowledgeSession;
import org.jboss.seam.drools.bootstrap.DroolsExtension;
import org.jboss.seam.drools.config.DroolsConfig;
import org.jboss.seam.drools.qualifiers.Scanned;
import org.jboss.weld.extensions.resourceLoader.ResourceProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author Tihomir Surdilovic
 */
@SessionScoped
public class KnowledgeSessionProducer implements Serializable
{
   private static final Logger log = LoggerFactory.getLogger(KnowledgeSessionProducer.class);

   @Inject
   BeanManager manager;
   
   @Inject
   ResourceProvider resourceProvider;
   
   @Inject
   DroolsExtension droolsExtension;
   
   @Inject 
   SeamDelegate delegate;

   @Produces
   @RequestScoped
   public StatefulKnowledgeSession produceStatefulSession(KnowledgeBase kbase,DroolsConfig config) throws Exception
   {
      StatefulKnowledgeSession ksession = kbase.newStatefulKnowledgeSession(config.getKnowledgeSessionConfiguration(), null);
      if(!config.isDisableSeamDelegate()) {
         addSeamDelegate(ksession);
      }
      addEventListeners(ksession);
      addWorkItemHandlers(ksession);
      addFactProviders(ksession);
      
      return ksession;
   }

   @Produces
   @Scanned
   @RequestScoped
   public StatefulKnowledgeSession produceScannedStatefulSession(@Scanned KnowledgeBase kbase, DroolsConfig config) throws Exception
   {
      StatefulKnowledgeSession ksession = kbase.newStatefulKnowledgeSession(config.getKnowledgeSessionConfiguration(), null);
      if(!config.isDisableSeamDelegate()) {
         addSeamDelegate(ksession);
      }
      addEventListeners(ksession);
      addWorkItemHandlers(ksession);
      addFactProviders(ksession);

      return ksession;
   }

   @Produces
   @Scanned
   @RequestScoped
   public StatelessKnowledgeSession produceScannedStatelessSession(@Scanned KnowledgeBase kbase, DroolsConfig config) throws Exception
   {
      StatelessKnowledgeSession ksession = kbase.newStatelessKnowledgeSession(config.getKnowledgeSessionConfiguration());
      if(!config.isDisableSeamDelegate()) {
         addSeamDelegate(ksession);
      }
      addEventListeners(ksession);
      
      return ksession;
   }

   @Produces
   @RequestScoped
   public StatelessKnowledgeSession produceStatelessSession(KnowledgeBase kbase, DroolsConfig config) throws Exception
   {
      StatelessKnowledgeSession ksession = kbase.newStatelessKnowledgeSession(config.getKnowledgeSessionConfiguration());
      if(!config.isDisableSeamDelegate()) {
         addSeamDelegate(ksession);
      }
      addEventListeners(ksession);
      
      return ksession;
   }

   public void disposeStatefulSession(@Disposes StatefulKnowledgeSession session)
   {
      session.dispose();
   }
   
   public void disposeScannedStatefulSession(@Disposes @Scanned StatefulKnowledgeSession session) {
      session.dispose();
   }
   
   private void addSeamDelegate(StatefulKnowledgeSession  ksession) {
      ksession.getGlobals().setDelegate(delegate);
   }
   
   private void addSeamDelegate(StatelessKnowledgeSession ksession) {
      ksession.getGlobals().setDelegate(delegate);
   }
   
   private void addEventListeners(KnowledgeRuntimeEventManager ksession)
   {
      Iterator<Object> iter = droolsExtension.getKsessionEventListenerSet().iterator();
      while (iter.hasNext())
      {
         Object eventListenerInstance = iter.next();

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

   private void addWorkItemHandlers(StatefulKnowledgeSession ksession)
   {
      Iterator<String> iter = droolsExtension.getWorkItemHandlers().keySet().iterator();
      while (iter.hasNext())
      {
         String name = iter.next();
         ksession.getWorkItemManager().registerWorkItemHandler(name, droolsExtension.getWorkItemHandlers().get(name));
      }
   }
   
   private void addFactProviders(StatefulKnowledgeSession ksession) {
      Iterator<FactProvider> iter = droolsExtension.getFactProviderSet().iterator();
      while(iter.hasNext())
      {
         FactProvider factProvider = iter.next();
         if(factProvider.getGlobals() != null) {
            Iterator<Entry<String, Object>> globalIterator = factProvider.getGlobals().entrySet().iterator();
            while(globalIterator.hasNext()) {
               Entry<String, Object> nextEntry = globalIterator.next();
               ksession.setGlobal(nextEntry.getKey(), nextEntry.getValue());
            }
         }
         
         if(factProvider.getFacts() != null) {
            for(Object nextFact : factProvider.getFacts()) {
               ksession.insert(nextFact);
            }
         }
      }
   }
}
