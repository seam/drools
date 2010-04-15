package org.jboss.seam.drools.test.kbase;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import javax.inject.Inject;

import org.drools.KnowledgeBase;
import org.jboss.arquillian.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.seam.drools.KnowledgeBaseProducer;
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
      .addPackages(true, new KBaseTestFilter(), KnowledgeBaseProducer.class.getPackage())
      .addPackages(true, ResourceProvider.class.getPackage())
      .addClass(KBaseTestRules.class)
      .addClass(MyKnowledgeBaseEventListener.class)
      .addResource(pkgPath + "/kbasetest.drl", ArchivePaths.create("kbasetest.drl"))
      .addResource(pkgPath + "/kbuilderconfig.properties", ArchivePaths.create("kbuilderconfig.properties"))
      .addResource(pkgPath + "/kbaseconfig.properties", ArchivePaths.create("kbaseconfig.properties"))
      .addManifestResource(pkgPath + "/KBaseTest-beans.xml", ArchivePaths.create("beans.xml"));
      System.out.println(archive.toString(Formatters.VERBOSE));
      return archive;
   }

   @Inject @KBaseTestRules KnowledgeBase kbase;
   
   @Test
   public void testKBase()
   {
      assertNotNull(kbase);
      assertTrue(kbase.getKnowledgePackage("org.jboss.seam.drools.test.kbase").getRules().size() == 3);
   }
}
