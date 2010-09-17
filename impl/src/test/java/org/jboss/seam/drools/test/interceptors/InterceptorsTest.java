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
package org.jboss.seam.drools.test.interceptors;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertNotSame;

import java.util.Collection;

import javax.enterprise.inject.Default;

import org.drools.runtime.ObjectFilter;
import org.drools.runtime.StatefulKnowledgeSession;
import org.jboss.arquillian.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.seam.drools.KnowledgeBaseProducer;
import org.jboss.seam.drools.config.Drools;
import org.jboss.seam.drools.qualifiers.config.DefaultConfig;
import org.jboss.seam.drools.test.DroolsModuleFilter;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.weld.extensions.resourceLoader.ResourceProvider;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class InterceptorsTest
{
   @Deployment
   public static JavaArchive createTestArchive()
   {
      String pkgPath = InterceptorsTest.class.getPackage().getName().replaceAll("\\.", "/");
      JavaArchive archive = ShrinkWrap.create("test.jar", JavaArchive.class)
      /**.addPackages(true, new DroolsModuleFilter("interceptors"), KnowledgeBaseProducer.class.getPackage())
      .addPackages(true, ResourceProvider.class.getPackage())
      .addClass(Person.class).addClass(InterceptorsTestBean.class)
      .addClass(InterceptorsTestConfig.class)
      .addResource(pkgPath + "/interceptorstest.drl", ArchivePaths.create("interceptorstest.drl"))
      .addResource(pkgPath + "/interceptorstestcep.drl", ArchivePaths.create("interceptorstestcep.drl"))
      .addResource(pkgPath + "/interceptorstestflow.drl", ArchivePaths.create("interceptorstestflow.drl"))
      .addResource(pkgPath + "/interceptortests.rf", ArchivePaths.create("interceptortests.rf"))
      .addManifestResource(pkgPath + "/InterceptorsTest-beans.xml", ArchivePaths.create("beans.xml"))
      .addManifestResource("META-INF/services/javax.enterprise.inject.spi.Extension", ArchivePaths.create("services/javax.enterprise.inject.spi.Extension"));
      // System.out.println(archive.toString(Formatters.VERBOSE))**/;
      return archive;
   }
   
   /**

   @Test
   public void testInsertAndFire(InterceptorsTestBean ibean, @Default @DefaultConfig StatefulKnowledgeSession ksession)
   {
      assertNotNull(ibean);
      assertNotNull(ksession);

      ibean.getPerson();

      Collection<?> allPeople = ksession.getObjects(new ObjectFilter()
      {
         public boolean accept(Object object)
         {
            return object instanceof Person;
         }
      });
      
      Person p = (Person) allPeople.toArray(new Object[0])[0];
      assertNotNull(p);
      assertTrue(p.isEligible());
   }
   
   @Test
   public void testInsertAndFireEntryPoint(InterceptorsTestBean ibean, @Default @CEPPseudoClockConfig StatefulKnowledgeSession ksession)
   {
      assertNotNull(ibean);
      assertNotNull(ksession);

      ibean.getPersonForEntryPoint();

      Collection<?> allPeople = ksession.getWorkingMemoryEntryPoint("peopleStream").getObjects(new ObjectFilter()
      {
         public boolean accept(Object object)
         {
            return object instanceof Person;
         }
      });
      
      Person p = (Person) allPeople.toArray(new Object[0])[0];
      assertNotNull(p);
      assertTrue(p.isEligible());
   }
   
   @Test
   public void testProcessStartAndSignal(InterceptorsTestBean ibean,
         @Default @InterceptorsTestConfig StatefulKnowledgeSession ksession) {
      assertNotNull(ibean);
      assertNotNull(ksession);
      
      ibean.getPersonForFlow();
      Collection<?> allPeople1 = ksession.getObjects(new ObjectFilter()
      {
         public boolean accept(Object object)
         {
            return object instanceof Person;
         }
      });
      
      Person p1 = (Person) allPeople1.toArray(new Object[0])[0];
      assertNotNull(p1);
      assertTrue(!p1.isEligible());
   
      ibean.startProcess();
      
      Collection<?> allPeople2 = ksession.getObjects(new ObjectFilter()
      {
         public boolean accept(Object object)
         {
            return object instanceof Person;
         }
      });
      
      Person p2 = (Person) allPeople2.toArray(new Object[0])[0];
      assertNotNull(p2);
      assertTrue(p2.isEligible());

      

   }**/
   @Test
   public void nothingToTest() {
      
   }
}
