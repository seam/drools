/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc., and individual contributors
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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;

import org.drools.command.CommandFactory;
import org.drools.runtime.ExecutionResults;
import org.drools.runtime.StatelessKnowledgeSession;
import org.jboss.seam.drools.bootstrap.DroolsExtension;
import org.jboss.seam.drools.qualifiers.Scanned;
import org.jboss.weld.extensions.resources.ResourceProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author Tihomir Surdilovic
 */
@SessionScoped
public class ExecutionResultsProducer implements Serializable
{
   private static final Logger log = LoggerFactory.getLogger(ExecutionResultsProducer.class);

   @Inject
   BeanManager manager;
   
   @Inject
   ResourceProvider resourceProvider;
   
   @Inject
   DroolsExtension droolsExtension;
   
   @SuppressWarnings("unchecked")
   @Produces
   @RequestScoped
   public ExecutionResults produceExecutionResults(StatelessKnowledgeSession ksession) {
      return ksession.execute(CommandFactory.newBatchExecution(getCommandList()));
   }
   
   @SuppressWarnings("unchecked")
   @Produces
   @Scanned
   @RequestScoped
   public ExecutionResults produceScannedExecutionResults(@Scanned StatelessKnowledgeSession ksession) {
      return ksession.execute(CommandFactory.newBatchExecution(getCommandList()));
   }
   
   @SuppressWarnings("unchecked")
   private List getCommandList() {
      List commandList = new ArrayList();
      Iterator<FactProvider> iter = droolsExtension.getFactProviderSet().iterator();
      while(iter.hasNext())
      {
         FactProvider factProvider = iter.next();
         if(factProvider.getGlobals() != null) {
            Iterator<Entry<String, Object>> globalIterator = factProvider.getGlobals().entrySet().iterator();
            while(globalIterator.hasNext()) {
               Entry<String, Object> nextEntry = globalIterator.next();
               commandList.add(CommandFactory.newSetGlobal(nextEntry.getKey(), nextEntry.getValue()));
            }
         }
         
         if(factProvider.getFacts() != null) {
            for(Object nextFact : factProvider.getFacts()) {
               commandList.add(CommandFactory.newInsert(nextFact));
            }
         }
         if(factProvider.getQueries() != null) {
            for(Object nextQuery : factProvider.getQueries()) {
               commandList.add(CommandFactory.newQuery((String) nextQuery, (String) nextQuery));
            }
         }   
      }
      return commandList;
   }
   

}
