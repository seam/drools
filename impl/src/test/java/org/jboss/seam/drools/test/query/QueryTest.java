/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc., and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */ 
package org.jboss.seam.drools.test.query;

import org.jboss.arquillian.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class QueryTest
{
   @Deployment
   public static JavaArchive createTestArchive()
   {
      String pkgPath = QueryTest.class.getPackage().getName().replaceAll("\\.", "/");
      JavaArchive archive = ShrinkWrap.create(JavaArchive.class)
      /**.addPackages(true, new DroolsModuleFilter("query"), KnowledgeBaseProducer.class.getPackage())
      .addPackages(true, ResourceProvider.class.getPackage())
      .addClass(Person.class)
      .addClass(QueryFactProvider.class)
      .addResource(pkgPath + "/querytest.drl", ArchivePaths.create("querytest.drl"))
      //.addResource(pkgPath + "/kbuilderconfig.properties", ArchivePaths.create("kbuilderconfig.properties"))
      //.addResource(pkgPath + "/kbaseconfig.properties", ArchivePaths.create("kbaseconfig.properties"))
      .addManifestResource(pkgPath + "/QueryTest-beans.xml", ArchivePaths.create("beans.xml"))
      .addManifestResource("META-INF/services/javax.enterprise.inject.spi.Extension", ArchivePaths.create("services/javax.enterprise.inject.spi.Extension"));
      //System.out.println(archive.toString(Formatters.VERBOSE))**/;
      return archive;
   }

   /**
   // cannot yet move to test method arguments (ARQ-120)
   @Inject @Default @DefaultConfig @Query("number of adults") QueryResults adultsQuery;
   @Inject @Default @DefaultConfig @Query("number of minors") QueryResults minorsQuery;
   @Inject @Default @Stateless @DefaultConfig ExecutionResults executionResults;
   
   @Test
   public void testQuery() {
      assertNotNull(adultsQuery);
      assertNotNull(minorsQuery);
      assertNotSame(adultsQuery, minorsQuery);
      
      assertTrue(adultsQuery.size() == 7);
      assertTrue(minorsQuery.size() == 7);
      
      assertNotNull(executionResults);
      assertTrue(((QueryResults) executionResults.getValue("number of adults")).size() == 7);
      assertTrue(((QueryResults) executionResults.getValue("number of minors")).size() == 7);
      
      
   }**/
   @Test
   public void nothingToTest() {
      
   }
}
