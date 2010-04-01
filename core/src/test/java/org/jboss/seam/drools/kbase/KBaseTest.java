package org.jboss.seam.drools.kbase;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.util.AnnotationLiteral;
import javax.inject.Inject;

import org.drools.KnowledgeBase;
import org.jboss.arquillian.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.seam.drools.KnowledgeBaseProducer;
import org.jboss.seam.drools.config.KnowledgeBaseConfig;
import org.jboss.seam.drools.qualifier.KBaseConfig;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.Archives;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class KBaseTest
{
   @Deployment
   public static JavaArchive createTestArchive()
   {
      return Archives.create("test.jar", JavaArchive.class)
         .addPackages(true, KnowledgeBaseProducer.class.getPackage())
         .addResource(KBaseTest.class.getPackage().getName().replaceAll("\\.", "/") + "/kbasetest.drl")
         .addResource(KBaseTest.class.getPackage().getName().replaceAll("\\.", "/") + "/kbuilderconfig.properties") 
         .addResource(KBaseTest.class.getPackage().getName().replaceAll("\\.", "/") + "/kbaseconfig.properties")
         .addManifestResource(KBaseTest.class.getPackage().getName().replaceAll("\\.", "/") + "/KBaseTest-beans.xml", 
               ArchivePaths.create("beans.xml"));
   }
  
   @Inject BeanManager manager;
   
   @Test
   public void testManager() {
      assertNotNull(manager);
   }
   
   @Test
   public void testKBaseConfig() {
      Bean<KnowledgeBaseConfig> knowledgeBaseConfigBean = (Bean<KnowledgeBaseConfig>) 
      manager.getBeans(KnowledgeBaseConfig.class, new KBaseConfigBinding("kbaseconfig1")).iterator().next();
      
      KnowledgeBaseConfig kbaseConfig = (KnowledgeBaseConfig) manager.getReference(knowledgeBaseConfigBean, KnowledgeBaseConfig.class, manager.createCreationalContext(knowledgeBaseConfigBean));
      
      assertNotNull(kbaseConfig);
      System.out.println("**** " + kbaseConfig.getEventListeners());
   }
   
   //@Test
   public void testKBase() {
      //assertNotNull(kbasebean.getKbase());
      //System.out.println("********* KBASE: " + kbasebean.getKbase());
   }
   
   static class KBaseConfigBinding extends AnnotationLiteral<KBaseConfig> implements KBaseConfig
   {
      private String value = null;
      public KBaseConfigBinding(String value)
      {
         this.value = value;
      }
      
      public String name() {
         return value;
      }
   }
}

