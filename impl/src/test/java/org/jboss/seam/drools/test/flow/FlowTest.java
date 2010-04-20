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
package org.jboss.seam.drools.test.flow;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertNotSame;

import java.util.ArrayList;

import javax.enterprise.inject.Default;
import javax.inject.Inject;

import org.drools.runtime.StatefulKnowledgeSession;
import org.jboss.arquillian.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.seam.drools.KnowledgeBaseProducer;
import org.jboss.seam.drools.qualifiers.config.DefaultConfig;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.Archives;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.weld.extensions.resources.ResourceProvider;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class FlowTest
{
   @Deployment
   public static JavaArchive createTestArchive()
   {
      String pkgPath = FlowTest.class.getPackage().getName().replaceAll("\\.", "/");
      JavaArchive archive = Archives.create("test.jar", JavaArchive.class)
      .addPackages(true, new FlowTestFilter(), KnowledgeBaseProducer.class.getPackage())
      .addPackages(true, ResourceProvider.class.getPackage())
      .addClass(Person.class)
      .addResource(pkgPath + "/flowtest.drl", ArchivePaths.create("flowtest.drl"))
      .addResource(pkgPath + "/flowtest.rf", ArchivePaths.create("flowtest.rf"))
      //.addResource(pkgPath + "/kbuilderconfig.properties", ArchivePaths.create("kbuilderconfig.properties"))
      //.addResource(pkgPath + "/kbaseconfig.properties", ArchivePaths.create("kbaseconfig.properties"))
      .addManifestResource(pkgPath + "/FlowTest-beans.xml", ArchivePaths.create("beans.xml"));
      //System.out.println(archive.toString(Formatters.VERBOSE));
      return archive;
   }
   
   @Inject @Default @DefaultConfig StatefulKnowledgeSession ksession;
   
   @SuppressWarnings("unchecked")
   @Test
   public void testFlow() {
      assertNotNull(ksession);
      ksession.setGlobal("errors", new ArrayList<String>());
      ksession.insert(new Person("Tihomir", ""));     
      
      ksession.startProcess("validationflow");
      ksession.fireAllRules();
      
      assertTrue( ((ArrayList<String>) ksession.getGlobal("errors")).size() == 1 );
      assertTrue(((ArrayList<String>) ksession.getGlobal("errors")).get(0).equals("You must enter a Telephone Number"));
     
   
   }
}
