package org.jboss.seam.drools.test.insertion;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import javax.inject.Inject;

import org.jboss.arquillian.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.seam.drools.interceptor.InsertInterceptor;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.Archives;
import org.jboss.shrinkwrap.api.formatter.Formatters;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class InsertInterceptorTest
{
   @Deployment
   public static JavaArchive createTestArchive()
   {
      String pkgPath = InsertInterceptorTest.class.getPackage().getName().replaceAll("\\.", "/");
      JavaArchive archive = Archives.create("test.jar", JavaArchive.class)
      .addClasses(InsertInterceptor.class, InsertionBean.class)
      .addManifestResource(pkgPath + "/InsertInterceptorTest-beans.xml", ArchivePaths.create("beans.xml"));
      System.out.println(archive.toString(Formatters.VERBOSE));
      return archive;
   }
   
   @Inject InsertionBean insertionBean;
   
   @Test
   public void testInsertFactHappens() {
      String result = insertionBean.insertResultAsFact();
      assertTrue(result.equals("abc")); //TODO finish this test
   }
}
