package org.jboss.seam.drools;

import java.io.InputStream;
import java.util.Properties;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;

import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseConfiguration;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderConfiguration;
import org.drools.builder.KnowledgeBuilderError;
import org.drools.builder.KnowledgeBuilderErrors;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.event.knowledgebase.KnowledgeBaseEventListener;
import org.drools.io.ResourceFactory;
import org.jboss.seam.drools.config.KnowledgeBaseConfig;
import org.jboss.seam.drools.events.KnowledgeBuilderErrorsEvent;
import org.jboss.seam.drools.events.RuleResourceAddedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author Tihomir Surdilovic
 */
public class KnowledgeBaseProducer
{
   private static final Logger log = LoggerFactory.getLogger(KnowledgeBaseProducer.class);
   @Inject BeanManager manager;

   @Produces //@ApplicationScoped
   public KnowledgeBase produceKBase(KnowledgeBaseConfig kbaseConfig) throws Exception
   {
      System.out.println("***KBASEPRODUCER CONFIG IS: " + kbaseConfig);
      KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder(getKnowledgeBuilderConfiguration(kbaseConfig));
      
      for (String nextResource : kbaseConfig.getRuleResources())
      {
         addResource(kbuilder, nextResource);
      }
      
      KnowledgeBuilderErrors kbuildererrors = kbuilder.getErrors();
      if (kbuildererrors.size() > 0)
      {
         for (KnowledgeBuilderError kbuildererror : kbuildererrors)
         {
            log.error(kbuildererror.getMessage());
         }
         manager.fireEvent(new KnowledgeBuilderErrorsEvent(kbuildererrors));
      }
      
      KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase(getKnowledgeBaseConfiguration(kbaseConfig));
      kbase.addKnowledgePackages(kbuilder.getKnowledgePackages());

      if (kbaseConfig.getEventListeners() != null)
      {
         for (String eventListener : kbaseConfig.getEventListeners())
         {
            addEventListener(kbase, eventListener);
         }
      }
      return kbase;
   }

   private KnowledgeBuilderConfiguration getKnowledgeBuilderConfiguration(KnowledgeBaseConfig kbaseConfig) throws Exception
   {
      KnowledgeBuilderConfiguration droolsKbuilderConfig = KnowledgeBuilderFactory.newKnowledgeBuilderConfiguration();   
      if (kbaseConfig.getKnowledgeBuilderConfig() != null && kbaseConfig.getKnowledgeBuilderConfig().endsWith(".properties"))
      {
         Properties kbuilderProp = new Properties();
         InputStream in = this.getClass().getClassLoader().getResourceAsStream(kbaseConfig.getKnowledgeBuilderConfig());
         if (in == null)
         {
            throw new IllegalStateException("Could not locate knowledgeBuilderConfig: " + kbaseConfig.getKnowledgeBuilderConfig());
         }
         kbuilderProp.load(in);
         in.close();
         droolsKbuilderConfig = KnowledgeBuilderFactory.newKnowledgeBuilderConfiguration(kbuilderProp, null);
         log.debug("KnowledgeBuilderConfiguration loaded: " + kbaseConfig.getKnowledgeBuilderConfig());
      }
      return droolsKbuilderConfig;
   }
   
   public KnowledgeBaseConfiguration getKnowledgeBaseConfiguration(KnowledgeBaseConfig kbaseConfig) throws Exception
   {
      KnowledgeBaseConfiguration droolsKbaseConfig = KnowledgeBaseFactory.newKnowledgeBaseConfiguration();
      if (kbaseConfig.getKnowledgeBaseConfig() != null && kbaseConfig.getKnowledgeBaseConfig().endsWith(".properties"))
      {
         Properties kbaseProp = new Properties();
         InputStream in = this.getClass().getClassLoader().getResourceAsStream(kbaseConfig.getKnowledgeBaseConfig());
         if (in == null)
         {
            throw new IllegalStateException("Could not locate knowledgeBaseConfig: " + kbaseConfig.getKnowledgeBaseConfig());
         }
         kbaseProp.load(in);
         in.close();
         droolsKbaseConfig = KnowledgeBaseFactory.newKnowledgeBaseConfiguration(kbaseProp, null);
         log.debug("KnowledgeBaseConfiguration loaded: " + kbaseConfig.getKnowledgeBaseConfig());
      }
      return droolsKbaseConfig;
   }
   
   private void addResource(KnowledgeBuilder kbuilder, String resource) throws Exception
   {
      //TODO add support for drools templates definition! 
      ResourceType resourceType = ResourceType.getResourceType(KnowledgeBaseConfig.getResourceType(resource));
      if(KnowledgeBaseConfig.isValidResource(resource)) {
         if (KnowledgeBaseConfig.getResourcePath(resource).equals(KnowledgeBaseConfig.RESOURCE_TYPE_URL))
         {
            kbuilder.add(ResourceFactory.newUrlResource(KnowledgeBaseConfig.getRuleResource(resource)), resourceType);
            manager.fireEvent(new RuleResourceAddedEvent(KnowledgeBaseConfig.getRuleResource(resource)));
         }
         else if (KnowledgeBaseConfig.getResourcePath(resource).equals(KnowledgeBaseConfig.RESOURCE_TYPE_FILE))
         {
            kbuilder.add(ResourceFactory.newFileResource(KnowledgeBaseConfig.getRuleResource(resource)), resourceType);
            manager.fireEvent(new RuleResourceAddedEvent(KnowledgeBaseConfig.getRuleResource(resource)));
         }
         else if (KnowledgeBaseConfig.getResourcePath(resource).equals(KnowledgeBaseConfig.RESOURCE_TYPE_CLASSPATH))
         {
            kbuilder.add(ResourceFactory.newClassPathResource(KnowledgeBaseConfig.getRuleResource(resource)), resourceType);
            manager.fireEvent(new RuleResourceAddedEvent(KnowledgeBaseConfig.getRuleResource(resource)));
         }
         else
         {
            log.error("Invalid resource path: " + KnowledgeBaseConfig.getResourcePath(resource));
         }
      } else {
         log.error("Invalid resource definition: " + resource);
      }
   }
   
   private void addEventListener(org.drools.KnowledgeBase kbase, String eventListener) {
      try {
         @SuppressWarnings("unchecked")
         Class eventListenerClass = Class.forName(eventListener);
         Object eventListenerObject = eventListenerClass.newInstance();
        
         if(eventListenerObject instanceof KnowledgeBaseEventListener) {
            kbase.addEventListener((KnowledgeBaseEventListener) eventListenerObject);
         } else {
            log.debug("Event Listener " + eventListener + " is not of type KnowledgeBaseEventListener");
         }
      } catch(Exception e) {
         log.error("Error adding event listener " + e.getMessage());
      }
   }
}
