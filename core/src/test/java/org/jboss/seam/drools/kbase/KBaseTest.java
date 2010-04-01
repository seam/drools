package org.jboss.seam.drools.kbase;

import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import static org.junit.Assert.assertNotNull;

import javax.enterprise.util.AnnotationLiteral;
import javax.inject.Inject;
import junit.framework.Assert;

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
      String pkgPath = KBaseTest.class.getPackage().getName().replaceAll("\\.", "/");
      return Archives.create("test.jar", JavaArchive.class)
         .addPackages(true, KnowledgeBaseProducer.class.getPackage())
         .addResource(pkgPath + "/kbasetest.drl")
         .addResource(pkgPath + "/kbuilderconfig.properties") 
         .addResource(pkgPath + "/kbaseconfig.properties")
         .addManifestResource("META-INF/beans.xml", ArchivePaths.create("beans.xml"));
         // the XML bean config module doesn't pick up the beans.xml unless it's located at src/test/resources/META-INF/beans.xml
         //.addManifestResource(pkgPath + "/KBaseTest-beans.xml", ArchivePaths.create("beans.xml"));
   }

   @Inject @Any Instance<KnowledgeBaseConfig> kbaseConfigResolver;
   @Inject @KBaseConfig(name = "kbaseconfig1") KnowledgeBaseConfig config;
   
   @Test
   public void testKBaseConfig() {
      Assert.assertFalse(kbaseConfigResolver.select(new KBaseConfigBinding("kbaseconfig1")).isUnsatisfied());
      KnowledgeBaseConfig kbaseConfig = kbaseConfigResolver.select(new KBaseConfigBinding("kbaseconfig1")).get();
      assertNotNull(kbaseConfig);
      System.out.println("**** " + kbaseConfig.getEventListeners());
   }
   
   static class KBaseConfigBinding extends AnnotationLiteral<KBaseConfig> implements KBaseConfig
   {
      private String name = null;
      public KBaseConfigBinding(String name)
      {
         this.name = name;
      }
      
      public String name() {
         return name;
      }
   }
}

