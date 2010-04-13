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

import org.drools.event.knowledgebase.KnowledgeBaseEventListener;
import org.drools.runtime.process.WorkItemHandler;
import org.jboss.seam.drools.TemplateDataProvider;
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
      
   }

   public Set<KnowledgeBaseEventListener> getKbaseEventListenerSet()
   {
      return kbaseEventListenerSet;
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
   
}
