package org.jboss.seam.drools.kbase;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import javax.inject.Inject;

import org.drools.KnowledgeBase;
import org.jboss.arquillian.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.seam.drools.KnowledgeBaseProducer;
import org.jboss.seam.drools.qualifier.KBase;
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
      return Archives.create("kbasetest.jar", JavaArchive.class)
         .addPackages(true, KnowledgeBaseProducer.class.getPackage())
         .addResource(KBaseTest.class.getPackage().getName().replaceAll("\\.", "/") + "/kbasetest.drl")
         .addResource(KBaseTest.class.getPackage().getName().replaceAll("\\.", "/") + "/kbuilderconfig.properties") 
         .addResource(KBaseTest.class.getPackage().getName().replaceAll("\\.", "/") + "/kbaseconfig.properties")
         .addManifestResource(KBaseTest.class.getPackage().getName().replaceAll("\\.", "/") + "/KBaseTest-beans.xml", 
               ArchivePaths.create("beans.xml"));
   }
   
   @Inject @KBase(name="kbase1") KnowledgeBase kbase;
   
   @Test
   public void testKBase() {
      assertNotNull(kbase);
   }
}

