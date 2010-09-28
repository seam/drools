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

import java.io.StringReader;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.spi.AfterBeanDiscovery;
import javax.enterprise.inject.spi.AnnotatedMethod;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessAnnotatedType;
import javax.enterprise.util.AnnotationLiteral;

import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseConfiguration;
import org.drools.KnowledgeBaseFactory;
import org.drools.runtime.Channel;
import org.drools.runtime.Environment;
import org.drools.runtime.KnowledgeSessionConfiguration;
import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.base.evaluators.EvaluatorDefinition;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderError;
import org.drools.builder.KnowledgeBuilderErrors;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.conf.EventProcessingOption;
import org.drools.event.knowledgebase.KnowledgeBaseEventListener;
import org.drools.io.ResourceFactory;
import org.drools.runtime.conf.ClockTypeOption;
import org.drools.runtime.process.WorkItemHandler;
import org.jboss.seam.drools.FactProvider;
import org.jboss.seam.drools.TemplateDataProvider;
import org.jboss.seam.drools.annotations.cep.Event;
import org.jboss.seam.drools.annotations.cep.Max;
import org.jboss.seam.drools.bootstrap.util.RuleBuilder;
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
   private Map<String, Channel> channels = new HashMap<String, Channel>();
   private Set<AnnotatedMethod<?>> cepAlertingRegistry = new HashSet<AnnotatedMethod<?>>();
   private KnowledgeBase cepAlertingKbase;
   private StatefulKnowledgeSession cepAlertingKSession;
   private RuleBuilder ruleBuilder = new RuleBuilder();
   
   @PostConstruct
   public void init() {
      
   }
   
   @SuppressWarnings("serial")
   void afterBeanDiscovery(@Observes AfterBeanDiscovery abd, BeanManager bm) {
      
      if(isEnabledCepAlerting()) {
         initCEPAlerting();
      }
      
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
      
      // Channels
      log.info("Start creating channel definitions");
      Set<Bean<?>> allChannels = bm.getBeans(org.drools.runtime.Channel.class, new AnnotationLiteral<Any>(){});
      if(allChannels != null) {
          Iterator<Bean<?>> iter = allChannels.iterator();
          while (iter.hasNext())
          {
             Bean<?> channel = iter.next();
             org.jboss.seam.drools.qualifiers.Channel evaluatorAnnotation = channel.getBeanClass().getAnnotation(org.jboss.seam.drools.qualifiers.Channel.class);
             String channelName = evaluatorAnnotation.value();
             if(channelName.length() > 0) {
                CreationalContext<?> context = bm.createCreationalContext(channel);
                org.drools.runtime.Channel channelInstance = (org.drools.runtime.Channel) bm.getReference(channel, org.drools.runtime.Channel.class, context);
                channels.put(channelName, channelInstance);
             } else {
                throw new IllegalStateException("Channel name cannot be empty in class: " + channel.getBeanClass().getName());
             }
          }
       }
      log.info("End creating [" + ( allChannels== null ? 0 : allChannels.size())+ "] channel definitions");      
      
      
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
   
   <X> void processAnnotatedType(@Observes ProcessAnnotatedType<X> pat, BeanManager manager) {   
      for(AnnotatedMethod<?> m : pat.getAnnotatedType().getMethods()) {
         if(m.isAnnotationPresent(Event.class)) { 
            cepAlertingRegistry.add(m);
            System.out.println("*** - " + pat.toString());
            System.out.println("*** Found Event: " + m.getClass().getName() + " - " + m.toString());
            Iterator<Annotation> iter = m.getAnnotations().iterator();
            while(iter.hasNext()) {
               Annotation a = iter.next();
               if(a instanceof Max) {
                  System.out.println("Max value: " + ((Max) a).value());
               }
               System.out.println("*** annotation class: " + a.getClass().getName());
            }
         }
      }
   }
   
   private void initCEPAlerting() {
      KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
      kbuilder.add(ResourceFactory.newReaderResource(new StringReader(ruleBuilder.getRuleDrl())), ResourceType.DRL);
      KnowledgeBuilderErrors errors = kbuilder.getErrors();
      if (errors.size() > 0) {
         for (KnowledgeBuilderError error: errors) {
            log.error(error.getMessage());
         }
         throw new IllegalArgumentException("Could not parse cepalerting knowledge.");
      }
      KnowledgeBaseConfiguration kbaseConfig = KnowledgeBaseFactory.newKnowledgeBaseConfiguration();
      kbaseConfig.setOption( EventProcessingOption.STREAM );
      
      cepAlertingKbase = KnowledgeBaseFactory.newKnowledgeBase(kbaseConfig);
      cepAlertingKbase.addKnowledgePackages(kbuilder.getKnowledgePackages());
      
      KnowledgeSessionConfiguration ksessionConfig = KnowledgeBaseFactory.newKnowledgeSessionConfiguration();
      if(usePseudoClock()) {
         ksessionConfig.setOption( ClockTypeOption.get("pseudo") );
      } else {
         ksessionConfig.setOption( ClockTypeOption.get("realtime") );
      }
      
      Environment env = KnowledgeBaseFactory.newEnvironment();

      cepAlertingKSession = cepAlertingKbase.newStatefulKnowledgeSession(ksessionConfig, env);
      cepAlertingKSession.fireUntilHalt();
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

   public static Logger getLog()
   {
      return log;
   }

   public Map<String, Channel> getChannels()
   {
      return channels;
   }

   public Set<AnnotatedMethod<?>> getCepAlertingRegistry()
   {
      return cepAlertingRegistry;
   }

   public KnowledgeBase getCepAlertingKbase()
   {
      return cepAlertingKbase;
   }

   public StatefulKnowledgeSession getCepAlertingKSession()
   {
      return cepAlertingKSession;
   }
   
   private boolean isEnabledCepAlerting() {
      String enabled = System.getProperty("seam.drools.cepalerting.enabled");
      if(enabled != null && enabled.equals("false")) {
         return false;
      } else {
         return true;
      }
   }
   
   private boolean usePseudoClock() {
      String pseudoClock = System.getProperty("seam.drools.cepalertingn.clock");
      if(pseudoClock != null && pseudoClock.equals("pseudo")) {
         return true;
      } else {
         return false;
      }
   }
   
}
