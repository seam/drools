package org.jboss.seam.drools.test.ksession;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import javax.inject.Inject;

import org.drools.runtime.StatefulKnowledgeSession;
import org.jboss.arquillian.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.seam.drools.KnowledgeBaseProducer;
import org.jboss.seam.drools.qualifiers.KBaseConfigured;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.Archives;
import org.jboss.shrinkwrap.api.formatter.Formatters;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.weld.extensions.resources.ResourceProvider;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class KSessionTest
{
   @Deployment
   public static JavaArchive createTestArchive()
   {
      String pkgPath = KSessionTest.class.getPackage().getName().replaceAll("\\.", "/");
      JavaArchive archive = Archives.create("test.jar", JavaArchive.class)
      .addPackages(true, new KSessionTestFiler(), KnowledgeBaseProducer.class.getPackage())
      .addPackages(true, ResourceProvider.class.getPackage())
      .addClass(KSessionTestQualifier.class)
      .addResource(pkgPath + "/ksessiontest.drl", ArchivePaths.create("ksessiontest.drl"))
      .addResource(pkgPath + "/kbuilderconfig.properties", ArchivePaths.create("kbuilderconfig.properties"))
      .addResource(pkgPath + "/kbaseconfig.properties", ArchivePaths.create("kbaseconfig.properties"))
      .addManifestResource(pkgPath + "/KSessionTest-beans.xml", ArchivePaths.create("beans.xml"));
      System.out.println(archive.toString(Formatters.VERBOSE));
      return archive;
   }
   
   @Inject 
   @KSessionTestQualifier 
   @KBaseConfigured 
   StatefulKnowledgeSession ksession;
   
   @Test
   public void testKSession()
   {
      assertNotNull(ksession);
   }
}
