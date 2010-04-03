package org.jboss.seam.drools.kbase;

import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import static org.junit.Assert.assertNotNull;

import javax.enterprise.util.AnnotationLiteral;
import javax.inject.Inject;
import junit.framework.Assert;

import org.drools.KnowledgeBase;
import org.jboss.arquillian.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.seam.drools.KnowledgeBaseProducer;
import org.jboss.seam.drools.config.KnowledgeBaseConfig;
import org.jboss.seam.drools.qualifier.KBase;
import org.jboss.seam.drools.qualifier.KBaseConfig;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.Archives;
import org.jboss.shrinkwrap.api.formatter.Formatters;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.weld.extensions.resources.ResourceProvider;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class KBaseTest
{
   @Deployment
   public static JavaArchive createTestArchive()
   {
      String pkgPath = KBaseTest.class.getPackage().getName().replaceAll("\\.", "/");
      JavaArchive archive = Archives.create("test.jar", JavaArchive.class)
         .addPackages(true, KnowledgeBaseProducer.class.getPackage())
         .addPackages(true, ResourceProvider.class.getPackage())
         .addResource(pkgPath + "/kbasetest.drl", ArchivePaths.create("kbasetest.drl"))
         .addResource(pkgPath + "/kbuilderconfig.properties", ArchivePaths.create("kbuilderconfig.properties"))
         .addResource(pkgPath + "/kbaseconfig.properties", ArchivePaths.create("kbaseconfig.properties"))
         .addManifestResource(pkgPath + "/KBaseTest-beans.xml", ArchivePaths.create("beans.xml"));
      System.out.println(archive.toString(Formatters.VERBOSE));
      return archive;
   }

   @Inject @Any Instance<KnowledgeBaseConfig> kbaseConfigResolver;
   @Inject @KBaseConfig("kbaseconfig1") KnowledgeBaseConfig config;
   
   @Inject @KBase("kbase1") KnowledgeBase kbase;
   
   @Test
   public void testKBaseConfig() {
      Assert.assertFalse(kbaseConfigResolver.select(new KBaseConfigBinding("kbaseconfig1")).isUnsatisfied());
      KnowledgeBaseConfig kbaseConfig = kbaseConfigResolver.select(new KBaseConfigBinding("kbaseconfig1")).get();
      assertNotNull(kbaseConfig);
      System.out.println("\n\n\n**** " + kbaseConfig.toString() + "********\n\n\n");
   }
   
   @Test
   public void testKBase() {
      assertNotNull(kbase);
   }
   
   static class KBaseConfigBinding extends AnnotationLiteral<KBaseConfig> implements KBaseConfig
   {
      private String value = null;
      public KBaseConfigBinding(String value)
      {
         this.value = value;
      }
      
      public String value() {
         return value;
      }
   }
}
