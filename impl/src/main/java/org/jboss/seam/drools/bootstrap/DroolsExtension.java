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
package org.jboss.seam.drools.bootstrap;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.spi.AfterBeanDiscovery;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.util.AnnotationLiteral;

import org.drools.base.evaluators.EvaluatorDefinition;
import org.drools.event.knowledgebase.KnowledgeBaseEventListener;
import org.drools.runtime.process.WorkItemHandler;
import org.jboss.seam.drools.FactProvider;
import org.jboss.seam.drools.TemplateDataProvider;
import org.jboss.seam.drools.qualifiers.EvaluatorDef;
import org.jboss.seam.drools.qualifiers.KBaseEventListener;
import org.jboss.seam.drools.qualifiers.KSessionEventListener;
import org.jboss.seam.drools.qualifiers.TemplateData;
import org.jboss.seam.drools.qualifiers.WIHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DroolsExtension implements Extension
{
   private static final Logger log = LoggerFactory.getLogger(DroolsExtension.class);
   private Set<KnowledgeBaseEventListener> kbaseEventListenerSet = new HashSet<KnowledgeBaseEventListener>();
   private Set<Object> ksessionEventListenerSet = new HashSet<Object>();
   private Map<String, WorkItemHandler> workItemHandlers = new HashMap<String, WorkItemHandler>();
   private Map<String, TemplateDataProvider> templateDataProviders = new HashMap<String, TemplateDataProvider>();
   private Set<FactProvider> factProviderSet = new HashSet<FactProvider>();
   private Map<String, EvaluatorDefinition> evaluatorDefinitions = new HashMap<String, EvaluatorDefinition>();
   
   @SuppressWarnings("serial")
   void afterBeanDiscovery(@Observes AfterBeanDiscovery abd, BeanManager bm) {
      //KnowledgeBase event listeners
      log.info("Start creating knowledgebase event listeners");
      Set<Bean<?>> allKBaseEventListeners = bm.getBeans(KnowledgeBaseEventListener.class, new AnnotationLiteral<KBaseEventListener>() {});
      if(allKBaseEventListeners != null) {
         Iterator<Bean<?>> kbaseEventListenerIterator = allKBaseEventListeners.iterator();
         while (kbaseEventListenerIterator.hasNext()) {
            Bean<?> listener = kbaseEventListenerIterator.next();
            CreationalContext<?> context = bm.createCreationalContext(listener);
            kbaseEventListenerSet.add((KnowledgeBaseEventListener) bm.getReference(listener, KnowledgeBaseEventListener.class, context));
            
         }
      }
      log.info("End creating [" + (allKBaseEventListeners == null ? 0 : allKBaseEventListeners.size())+ "] knowledgebase event listeners");      
      
      //KnowledgeSession event listeners
      log.info("Start creating knowledgeSession event listeners");
      Set<Bean<?>> allKSessionEventListeners = bm.getBeans(Object.class, new AnnotationLiteral<KSessionEventListener>() {});
      if(allKBaseEventListeners != null) {
         Iterator<Bean<?>> ksessionEventListenerIterator = allKSessionEventListeners.iterator();
         while (ksessionEventListenerIterator.hasNext()) {
            Bean<?> listener = ksessionEventListenerIterator.next();
            CreationalContext<?> context = bm.createCreationalContext(listener);
            ksessionEventListenerSet.add(bm.getReference(listener, Object.class, context));        
         }
      }
      log.info("End creating [" + (allKSessionEventListeners == null ? 0 : allKSessionEventListeners.size())+ "] knowledgesession event listeners");      
 
      //WorkItemHandlers
      log.info("Start creating workitem handlers");
      Set<Bean<?>> allWorkItemHandlers = bm.getBeans(WorkItemHandler.class, new AnnotationLiteral<Any>(){});
      if(allWorkItemHandlers != null) {
         Iterator<Bean<?>> iter = allWorkItemHandlers.iterator();
         while (iter.hasNext())
         {
            Bean<?> handler = iter.next();
            WIHandler handlerAnnotation = handler.getBeanClass().getAnnotation(WIHandler.class);
            String handlerName = handlerAnnotation.value();
            if(handlerName.length() > 0) {
               CreationalContext<?> context = bm.createCreationalContext(handler);
               WorkItemHandler handlerInstance = (WorkItemHandler) bm.getReference(handler, WorkItemHandler.class, context);
               workItemHandlers.put(handlerName, handlerInstance);
            } else {
               throw new IllegalStateException("WorkItemHandler name cannot be empty in class: " + handler.getBeanClass().getName());
            }
         }
      }
      log.info("End creating [" + (allWorkItemHandlers == null ? 0 : allWorkItemHandlers.size())+ "] workitem handlers");      
      
      //EvaluatorDefinitions
      log.info("Start creating evaluator definitions");
      Set<Bean<?>> allEvaluatorDefinitions = bm.getBeans(EvaluatorDefinition.class, new AnnotationLiteral<Any>(){});
      if(allEvaluatorDefinitions != null) {
          Iterator<Bean<?>> iter = allEvaluatorDefinitions.iterator();
          while (iter.hasNext())
          {
             Bean<?> evaluator = iter.next();
             EvaluatorDef evaluatorAnnotation = evaluator.getBeanClass().getAnnotation(EvaluatorDef.class);
             String evaluatorName = evaluatorAnnotation.value();
             if(evaluatorName.length() > 0) {
                CreationalContext<?> context = bm.createCreationalContext(evaluator);
                EvaluatorDefinition evaluatorInstance = (EvaluatorDefinition) bm.getReference(evaluator, EvaluatorDefinition.class, context);
                evaluatorDefinitions.put(evaluatorName, evaluatorInstance);
             } else {
                throw new IllegalStateException("Evaluator name cannot be empty in class: " + evaluator.getBeanClass().getName());
             }
          }
       }
      log.info("End creating [" + ( allEvaluatorDefinitions== null ? 0 : allEvaluatorDefinitions.size())+ "] evaluator definitions");      
      
      
      //Template Data Providers
      log.info("Start creating template providers");
      Set<Bean<?>> allTemplateProviders = bm.getBeans(TemplateDataProvider.class, new AnnotationLiteral<Any>(){});
      if(allTemplateProviders != null) {
         Iterator<Bean<?>> iter = allTemplateProviders.iterator();
         while (iter.hasNext())
         {
            Bean<?> templateDataProvider = iter.next();
            TemplateData teamplateDataAnnotation = templateDataProvider.getBeanClass().getAnnotation(TemplateData.class);
            String templateDataName = teamplateDataAnnotation.value();
            if(templateDataName.length() > 0) {
               CreationalContext<?> context = bm.createCreationalContext(templateDataProvider);
               TemplateDataProvider templateDataProviderInstance = (TemplateDataProvider) bm.getReference(templateDataProvider, TemplateDataProvider.class, context);
               templateDataProviders.put(templateDataName, templateDataProviderInstance);
            } else {
               throw new IllegalStateException("TemplateDataProvider name cannot be empty in class: " + templateDataProvider.getBeanClass().getName());
            }
         }
      }
      log.info("End creating [" + (allTemplateProviders == null ? 0 : allTemplateProviders.size())+ "] template data providers");      
      
      //Fact Providers
      log.info("Start creating fact providers");
      Set<Bean<?>> allFactProviders = bm.getBeans(FactProvider.class, new AnnotationLiteral<Any>() {});
      if(allFactProviders != null) {
         Iterator<Bean<?>> factProviderIterator = allFactProviders.iterator();
         while (factProviderIterator.hasNext()) {
            Bean<?> factProvider = factProviderIterator.next();
            CreationalContext<?> context = bm.createCreationalContext(factProvider);
            factProviderSet.add((FactProvider) bm.getReference(factProvider, FactProvider.class, context));
         }
      }
      log.info("End creating [" + (allFactProviders == null ? 0 : allFactProviders.size())+ "] fact providers");      
      
   }

   public Set<KnowledgeBaseEventListener> getKbaseEventListenerSet()
   {
      return kbaseEventListenerSet;
   }
   
   public Set<FactProvider> getFactProviderSet() 
   {
      return factProviderSet;
   }

   public Map<String, WorkItemHandler> getWorkItemHandlers()
   {
      return workItemHandlers;
   }

   public Set<Object> getKsessionEventListenerSet()
   {
      return ksessionEventListenerSet;
   }

   public Map<String, TemplateDataProvider> getTemplateDataProviders()
   {
      return templateDataProviders;
   }

   public Map<String, EvaluatorDefinition> getEvaluatorDefinitions() {
	   return evaluatorDefinitions;
   }  
}
