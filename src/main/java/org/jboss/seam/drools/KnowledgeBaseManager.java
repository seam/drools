package org.jboss.seam.drools;

import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.util.Properties;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import javax.security.auth.login.Configuration;

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
import org.drools.template.ObjectDataCompiler;
import org.jboss.seam.drools.events.KnowledgeBuilderErrorsEvent;
import org.jboss.seam.drools.events.RuleResourceAddedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Manager component for a Drools KnowledgeBase.
 * 
 * @author Tihomir Surdilovic
 */
@Dependent
public class KnowledgeBaseManager
{
   private static final Logger log = LoggerFactory.getLogger(KnowledgeBaseManager.class);

   private static final Pattern DIVIDER = Pattern.compile(";");
   private static final int RESOURCE_PATH = 0;
   private static final int RESOURCE = 1;
   private static final int RESOURCE_TYPE = 2;
   private static final int RESOURCE_TEMPLATE_DATA = 3;
   private static final String RESOURCE_TYPE_URL = "url";
   private static final String RESOURCE_TYPE_FILE = "file";
   private static final String RESOURCE_TYPE_CLASSPATH = "classpath";

   private String knowledgeBuilderConfig;
   private String knowledgeBaseConfig;
   private String[] ruleResources;
   private String[] eventListeners;
   private KnowledgeBase kbase;

   @Inject
   BeanManager manager;

   @Produces
   @ApplicationScoped
   public KnowledgeBase getKBase()
   {
      return kbase;
   }

   public void disposeKBase(@Disposes KnowledgeBase kbase)
   {
      kbase = null;
   }

   @PostConstruct
   private void createKBase() throws Exception
   {
      KnowledgeBuilderConfiguration kbuilderconfig = KnowledgeBuilderFactory.newKnowledgeBuilderConfiguration();
      // Only allow resource for .properties files
      if (knowledgeBuilderConfig != null && knowledgeBuilderConfig.endsWith(".properties"))
      {
         Properties kbuilderProp = new Properties();
         InputStream in = this.getClass().getClassLoader().getResourceAsStream(knowledgeBuilderConfig);
         if (in == null)
         {
            throw new IllegalStateException("Could not locate knowledgeBuilderConfig: " + knowledgeBuilderConfig);
         }
         kbuilderProp.load(in);
         in.close();
         kbuilderconfig = KnowledgeBuilderFactory.newKnowledgeBuilderConfiguration(kbuilderProp, null);
         log.debug("KnowledgeBuilderConfiguration loaded: " + knowledgeBuilderConfig);
      }

      KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder(kbuilderconfig);

      if (ruleResources != null)
      {
         for (String nextResource : ruleResources)
         {
            addResource(kbuilder, nextResource);
         }
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

      KnowledgeBaseConfiguration kbaseconfig = KnowledgeBaseFactory.newKnowledgeBaseConfiguration();

      // Only allow resource for .properties files
      if (knowledgeBaseConfig != null && knowledgeBaseConfig.endsWith(".properties"))
      {
         Properties kbaseProp = new Properties();
         InputStream in = this.getClass().getClassLoader().getResourceAsStream(knowledgeBaseConfig);
         if (in == null)
         {
            throw new IllegalStateException("Could not locate knowledgeBaseConfig: " + knowledgeBaseConfig);
         }
         kbaseProp.load(in);
         in.close();
         kbaseconfig = KnowledgeBaseFactory.newKnowledgeBaseConfiguration(kbaseProp, null);
         log.debug("KnowledgeBaseConfiguration loaded: " + knowledgeBaseConfig);
      }

      kbase = KnowledgeBaseFactory.newKnowledgeBase(kbaseconfig);
      kbase.addKnowledgePackages(kbuilder.getKnowledgePackages());

      if (eventListeners != null)
      {
         for (String eventListener : eventListeners)
         {
            addEventListener(kbase, eventListener);
         }
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
   
   protected void addResource(KnowledgeBuilder kbuilder, String resource) throws Exception
   {
      String[] resourceParts = DIVIDER.split(resource.trim());

      if (resourceParts.length < 3)
      {
         log.error("Invalid resource definition: " + resource);
      }
      else
      {
         ResourceType resourceType = ResourceType.getResourceType(resourceParts[RESOURCE_TYPE]);

         if (resourceParts.length == 4)
         {
            @SuppressWarnings("unchecked")
            Bean<TemplateDataProvider> templateDataProviderBean = (Bean<TemplateDataProvider>) manager.getBeans(resourceParts[RESOURCE_TEMPLATE_DATA]).iterator().next();

            TemplateDataProvider templateDataProvider = (TemplateDataProvider) manager.getReference(templateDataProviderBean, Configuration.class, manager.createCreationalContext(templateDataProviderBean));

            InputStream templateStream = this.getClass().getClassLoader().getResourceAsStream(resourceParts[RESOURCE]);
            if (templateStream == null)
            {
               throw new IllegalStateException("Could not locate rule resource: " + resourceParts[RESOURCE]);
            }

            ObjectDataCompiler converter = new ObjectDataCompiler();
            String drl = converter.compile(templateDataProvider.getTemplateData(), templateStream);
            templateStream.close();
            log.debug("Generated following DRL from template: " + drl);
            Reader rdr = new StringReader(drl);

            kbuilder.add(ResourceFactory.newReaderResource(rdr), resourceType);
         }
         else
         {
            if (resourceParts[RESOURCE_PATH].equals(RESOURCE_TYPE_URL))
            {
               kbuilder.add(ResourceFactory.newUrlResource(resourceParts[RESOURCE]), resourceType);
               manager.fireEvent(new RuleResourceAddedEvent(resourceParts[RESOURCE]));
            }
            else if (resourceParts[RESOURCE_PATH].equals(RESOURCE_TYPE_FILE))
            {
               kbuilder.add(ResourceFactory.newFileResource(resourceParts[RESOURCE]), resourceType);
               manager.fireEvent(new RuleResourceAddedEvent(resourceParts[RESOURCE]));
            }
            else if (resourceParts[RESOURCE_PATH].equals(RESOURCE_TYPE_CLASSPATH))
            {
               kbuilder.add(ResourceFactory.newClassPathResource(resourceParts[RESOURCE]), resourceType);
               manager.fireEvent(new RuleResourceAddedEvent(resourceParts[RESOURCE]));
            }
            else
            {
               log.error("Invalid resource path: " + resourceParts[RESOURCE_PATH]);
            }
         }
      }
   }

   public String getKnowledgeBuilderConfig()
   {
      return knowledgeBuilderConfig;
   }

   @Inject
   public void setKnowledgeBuilderConfig(String knowledgeBuilderConfig)
   {
      this.knowledgeBuilderConfig = knowledgeBuilderConfig;
   }

   public String getKnowledgeBaseConfig()
   {
      return knowledgeBaseConfig;
   }

   @Inject
   public void setKnowledgeBaseConfig(String knowledgeBaseConfig)
   {
      this.knowledgeBaseConfig = knowledgeBaseConfig;
   }

   public String[] getRuleResources()
   {
      return ruleResources;
   }

   @Inject
   public void setRuleResources(String[] ruleResources)
   {
      this.ruleResources = ruleResources;
   }

   public String[] getEventListeners()
   {
      return eventListeners;
   }

   @Inject
   public void setEventListeners(String[] eventListeners)
   {
      this.eventListeners = eventListeners;
   }
   
   
}
