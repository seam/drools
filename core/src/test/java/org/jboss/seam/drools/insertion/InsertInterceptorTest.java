package org.jboss.seam.drools.insertion;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import javax.inject.Inject;

import org.jboss.arquillian.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.seam.drools.interceptor.InsertInterceptor;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.Archives;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class InsertInterceptorTest
{
   @Deployment
   public static JavaArchive createTestArchive()
   {
      return Archives.create("test.jar", JavaArchive.class).addClasses(InsertInterceptor.class, InsertionBean.class).addManifestResource(InsertInterceptorTest.class.getPackage().getName().replaceAll("\\.", "/") + "/InsertInterceptorTest-beans.xml", ArchivePaths.create("beans.xml"));
   }
   
   @Inject InsertionBean insertionBean;
   
   @Test
   public void testInsertFactHappens() {
      String result = insertionBean.insertResultAsFact();
      assertTrue(result.equals("abc")); //TODO finish this test
   }
}
