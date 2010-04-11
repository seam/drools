package org.jboss.seam.drools.old;

import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.util.Properties;

import javax.enterprise.context.ApplicationScoped;
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
public class KnowledgeBaseManager
{
   /**
    * private static final Logger log =
    * LoggerFactory.getLogger(KnowledgeBaseManager.class);
    * 
    * private KnowledgeBaseManagerConfig kbaseManagerConfig;
    * 
    * @Inject BeanManager manager;
    * @Inject public KnowledgeBaseManager(KnowledgeBaseManagerConfig
    *         kbaseManagerConfig) { this.kbaseManagerConfig =
    *         kbaseManagerConfig; }
    * 
    *         //@Produces //@ApplicationScoped public KnowledgeBase
    *         createKBase() throws Exception { KnowledgeBase kbase;
    *         KnowledgeBuilder kbuilder =
    *         KnowledgeBuilderFactory.newKnowledgeBuilder
    *         (getKnowledgeBuilderConfiguration());
    * 
    *         for (String nextResource : kbaseManagerConfig.getRuleResources())
    *         { addResource(kbuilder, nextResource); }
    * 
    *         KnowledgeBuilderErrors kbuildererrors = kbuilder.getErrors(); if
    *         (kbuildererrors.size() > 0) { for (KnowledgeBuilderError
    *         kbuildererror : kbuildererrors) {
    *         log.error(kbuildererror.getMessage()); } manager.fireEvent(new
    *         KnowledgeBuilderErrorsEvent(kbuildererrors)); }
    * 
    *         kbase =
    *         KnowledgeBaseFactory.newKnowledgeBase(getKnowledgeBaseConfiguration
    *         ()); kbase.addKnowledgePackages(kbuilder.getKnowledgePackages());
    * 
    *         if (kbaseManagerConfig.getEventListeners() != null) { for (String
    *         eventListener : kbaseManagerConfig.getEventListeners()) {
    *         addEventListener(kbase, eventListener); } } return kbase; }
    * 
    *         //public void disposeKBase(@Disposes KnowledgeBase kbase) //{ //
    *         kbase = null; // }
    * 
    *         private void addEventListener(org.drools.KnowledgeBase kbase,
    *         String eventListener) { try {
    * @SuppressWarnings("unchecked") Class eventListenerClass =
    *                                Class.forName(eventListener); Object
    *                                eventListenerObject =
    *                                eventListenerClass.newInstance();
    * 
    *                                if(eventListenerObject instanceof
    *                                KnowledgeBaseEventListener) {
    *                                kbase.addEventListener
    *                                ((KnowledgeBaseEventListener)
    *                                eventListenerObject); } else {
    *                                log.debug("Event Listener " + eventListener
    *                                +
    *                                " is not of type KnowledgeBaseEventListener"
    *                                ); } } catch(Exception e) {
    *                                log.error("Error adding event listener " +
    *                                e.getMessage()); } }
    * 
    *                                protected void addResource(KnowledgeBuilder
    *                                kbuilder, String resource) throws Exception
    *                                {
    *                                if(KnowledgeBaseManagerConfig.isValidResource
    *                                (resource)) { ResourceType resourceType =
    *                                ResourceType.getResourceType(
    *                                KnowledgeBaseManagerConfig
    *                                .getResourceType(resource));
    *                                if(KnowledgeBaseManagerConfig
    *                                .isRuleTemplate(resource)) {
    * @SuppressWarnings("unchecked") Bean<TemplateDataProvider>
    *                                templateDataProviderBean =
    *                                (Bean<TemplateDataProvider>)
    *                                manager.getBeans
    *                                (KnowledgeBaseManagerConfig.
    *                                getTemplateData(
    *                                resource)).iterator().next();
    * 
    *                                TemplateDataProvider templateDataProvider =
    *                                (TemplateDataProvider)
    *                                manager.getReference(
    *                                templateDataProviderBean,
    *                                Configuration.class,
    *                                manager.createCreationalContext
    *                                (templateDataProviderBean));
    * 
    *                                InputStream templateStream =
    *                                this.getClass()
    *                                .getClassLoader().getResourceAsStream
    *                                (KnowledgeBaseManagerConfig
    *                                .getRuleResource(resource)); if
    *                                (templateStream == null) { throw new
    *                                IllegalStateException
    *                                ("Could not locate rule resource: " +
    *                                KnowledgeBaseManagerConfig
    *                                .getRuleResource(resource)); }
    * 
    *                                ObjectDataCompiler converter = new
    *                                ObjectDataCompiler(); String drl =
    *                                converter.compile(templateDataProvider.
    *                                getTemplateData(), templateStream);
    *                                templateStream.close();
    *                                log.debug("Generated following DRL from template: "
    *                                + drl); Reader rdr = new StringReader(drl);
    * 
    *                                
    *                                kbuilder.add(ResourceFactory.newReaderResource
    *                                (rdr), resourceType); } else { if
    *                                (KnowledgeBaseManagerConfig
    *                                .getResourcePath(resource).equals(
    *                                KnowledgeBaseManagerConfig
    *                                .RESOURCE_TYPE_URL)) {
    *                                kbuilder.add(ResourceFactory
    *                                .newUrlResource(
    *                                KnowledgeBaseManagerConfig.getRuleResource
    *                                (resource)), resourceType);
    *                                manager.fireEvent(new
    *                                RuleResourceAddedEvent
    *                                (KnowledgeBaseManagerConfig
    *                                .getRuleResource(resource))); } else if
    *                                (KnowledgeBaseManagerConfig
    *                                .getResourcePath(resource).equals(
    *                                KnowledgeBaseManagerConfig
    *                                .RESOURCE_TYPE_FILE)) {
    *                                kbuilder.add(ResourceFactory
    *                                .newFileResource
    *                                (KnowledgeBaseManagerConfig.
    *                                getRuleResource(resource)), resourceType);
    *                                manager.fireEvent(new
    *                                RuleResourceAddedEvent
    *                                (KnowledgeBaseManagerConfig
    *                                .getRuleResource(resource))); } else if
    *                                (KnowledgeBaseManagerConfig
    *                                .getResourcePath(resource).equals(
    *                                KnowledgeBaseManagerConfig
    *                                .RESOURCE_TYPE_CLASSPATH)) {
    *                                kbuilder.add(ResourceFactory
    *                                .newClassPathResource
    *                                (KnowledgeBaseManagerConfig
    *                                .getRuleResource(resource)), resourceType);
    *                                manager.fireEvent(new
    *                                RuleResourceAddedEvent
    *                                (KnowledgeBaseManagerConfig
    *                                .getRuleResource(resource))); } else {
    *                                log.error("Invalid resource path: " +
    *                                KnowledgeBaseManagerConfig
    *                                .getResourcePath(resource)); } } } else {
    *                                log.error("Invalid resource definition: " +
    *                                resource); } }
    * 
    *                                public KnowledgeBuilderConfiguration
    *                                getKnowledgeBuilderConfiguration() throws
    *                                Exception { KnowledgeBuilderConfiguration
    *                                kbuilderconfig = KnowledgeBuilderFactory.
    *                                newKnowledgeBuilderConfiguration();
    *                                if(kbaseManagerConfig
    *                                .getKnowledgeBuilderConfigProp() != null) {
    *                                kbuilderconfig = KnowledgeBuilderFactory.
    *                                newKnowledgeBuilderConfiguration
    *                                (kbaseManagerConfig
    *                                .getKnowledgeBuilderConfigProp(), null);
    *                                log.debug(
    *                                "KnowledgeBuilderConfiguration loaded: " +
    *                                kbaseManagerConfig
    *                                .getKnowledgeBuilderConfigProp()); } else {
    *                                // Only allow resource for .properties
    *                                files if
    *                                (kbaseManagerConfig.getKnowledgeBuilderConfig
    *                                () != null && kbaseManagerConfig.
    *                                getKnowledgeBuilderConfig
    *                                ().endsWith(".properties")) { Properties
    *                                kbuilderProp = new Properties();
    *                                InputStream in =
    *                                this.getClass().getClassLoader
    *                                ().getResourceAsStream(kbaseManagerConfig.
    *                                getKnowledgeBuilderConfig()); if (in ==
    *                                null) { throw new
    *                                IllegalStateException("Could not locate knowledgeBuilderConfig: "
    *                                + kbaseManagerConfig.
    *                                getKnowledgeBuilderConfig()); }
    *                                kbuilderProp.load(in); in.close();
    *                                kbuilderconfig = KnowledgeBuilderFactory.
    *                                newKnowledgeBuilderConfiguration
    *                                (kbuilderProp, null);
    *                                log.debug("KnowledgeBuilderConfiguration loaded: "
    *                                + kbaseManagerConfig.
    *                                getKnowledgeBuilderConfig()); } } return
    *                                kbuilderconfig; }
    * 
    *                                public KnowledgeBaseConfiguration
    *                                getKnowledgeBaseConfiguration() throws
    *                                Exception { KnowledgeBaseConfiguration
    *                                kbaseconfig = KnowledgeBaseFactory.
    *                                newKnowledgeBaseConfiguration();
    * 
    *                                
    *                                if(kbaseManagerConfig.getKnowledgeBaseConfigProp
    *                                () != null) { kbaseconfig =
    *                                KnowledgeBaseFactory
    *                                .newKnowledgeBaseConfiguration
    *                                (kbaseManagerConfig
    *                                .getKnowledgeBaseConfigProp(), null);
    *                                log.debug
    *                                ("KnowledgeBaseConfiguration loaded: " +
    *                                kbaseManagerConfig
    *                                .getKnowledgeBaseConfigProp()); } else { //
    *                                Only allow resource for .properties files
    *                                if (kbaseManagerConfig.
    *                                getKnowledgeBaseConfig() != null &&
    *                                kbaseManagerConfig
    *                                .getKnowledgeBaseConfig().
    *                                endsWith(".properties")) { Properties
    *                                kbaseProp = new Properties(); InputStream
    *                                in = this.getClass().getClassLoader().
    *                                getResourceAsStream
    *                                (kbaseManagerConfig.getKnowledgeBaseConfig
    *                                ()); if (in == null) { throw new
    *                                IllegalStateException
    *                                ("Could not locate knowledgeBaseConfig: " +
    *                                kbaseManagerConfig
    *                                .getKnowledgeBaseConfig()); }
    *                                kbaseProp.load(in); in.close(); kbaseconfig
    *                                = KnowledgeBaseFactory.
    *                                newKnowledgeBaseConfiguration(kbaseProp,
    *                                null);
    *                                log.debug("KnowledgeBaseConfiguration loaded: "
    *                                + kbaseManagerConfig.getKnowledgeBaseConfig
    *                                ()); } } return kbaseconfig; }
    **/
}