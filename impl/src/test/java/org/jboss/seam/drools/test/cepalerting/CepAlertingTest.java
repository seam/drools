package org.jboss.seam.drools.test.cepalerting;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import javax.inject.Inject;

import org.jboss.arquillian.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.seam.drools.KnowledgeBaseProducer;
import org.jboss.seam.drools.test.DroolsModuleFilter;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class CepAlertingTest
{
   @Deployment
   public static JavaArchive createTestArchive()
   {
      String pkgPath = CepAlertingTest.class.getPackage().getName().replaceAll("\\.", "/");
      JavaArchive archive = ShrinkWrap.create(JavaArchive.class)
      .addPackages(true, new DroolsModuleFilter("cepvalidation"), KnowledgeBaseProducer.class.getPackage())
      .addClass(AlertingBean.class)
      .addManifestResource(pkgPath + "/CepAlertingTest-beans.xml", ArchivePaths.create("beans.xml"))
      .addManifestResource("META-INF/services/javax.enterprise.inject.spi.Extension", ArchivePaths.create("services/javax.enterprise.inject.spi.Extension"));
      //System.out.println(archive.toString(Formatters.VERBOSE));
      return archive;
   }
   
   @Inject
   AlertingBean ab;
   
   @Test
   public void checkAlertingBean() {
      assertNotNull(ab);
      ab.doSomething("something");
   }
}
