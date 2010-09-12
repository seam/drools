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
package org.jboss.seam.drools.test.channel;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import javax.enterprise.inject.Default;
import javax.inject.Inject;

import org.drools.runtime.StatefulKnowledgeSession;
import org.jboss.arquillian.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.seam.drools.KnowledgeBaseProducer;
import org.jboss.seam.drools.qualifiers.config.DefaultConfig;
import org.jboss.seam.drools.test.DroolsModuleFilter;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.weld.extensions.resourceLoader.ResourceProvider;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class ChannelTest
{
   @Deployment
   public static JavaArchive createTestArchive()
   {
      String pkgPath = ChannelTest.class.getPackage().getName().replaceAll("\\.", "/");
      JavaArchive archive = ShrinkWrap.create("test.jar", JavaArchive.class)
      .addPackages(true, new DroolsModuleFilter("channel"), KnowledgeBaseProducer.class.getPackage())
      .addPackages(true, ResourceProvider.class.getPackage())
      .addClass(ChannelBean.class)
      .addClass(Person.class)
      .addResource(pkgPath + "/channeltest.drl", ArchivePaths.create("channeltest.drl"))
      // .addResource(pkgPath + "/kbuilderconfig.properties",
      // ArchivePaths.create("kbuilderconfig.properties"))
      // .addResource(pkgPath + "/kbaseconfig.properties",
      // ArchivePaths.create("kbaseconfig.properties"))
      .addManifestResource(pkgPath + "/ChannelTest-beans.xml", ArchivePaths.create("beans.xml"));
      // System.out.println(archive.toString(Formatters.VERBOSE));
      return archive;
   }
   
   @Inject
   @Default
   @DefaultConfig
   StatefulKnowledgeSession ksession;
   
   @Inject ChannelBean channelBean;
   
   @Test
   public void testChannel()
   {
      assertNotNull(ksession);
      assertNotNull(channelBean);
      
      Person p1 = new Person();
      p1.setAge(12);
      
      Person p2 = new Person();
      p2.setAge(20);
      
      Person p3 = new Person();
      p3.setAge(4);
      
      Person p4 = new Person();
      p4.setAge(19);
      
      Person p5 = new Person();
      p5.setAge(33);
      
      Person p6 = new Person();
      p6.setAge(55);
      
      Person p7 = new Person();
      p7.setAge(15);
      
      Person p8 = new Person();
      p8.setAge(69);
      
      ksession.insert(p1);
      ksession.insert(p2);
      ksession.insert(p3);
      ksession.insert(p4);
      ksession.insert(p5);
      ksession.insert(p6);
      ksession.insert(p7);
      ksession.insert(p8);
      
      ksession.fireAllRules();
      
      assertTrue(channelBean.getEligiblesList().size() == 5);
      assertTrue(channelBean.getNotEligiblesList().size() == 3);
      
   }
}
