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
package org.jboss.seam.drools.test.kagent;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.fail;


import javax.enterprise.inject.Default;

import org.drools.agent.KnowledgeAgent;
import org.drools.io.ResourceFactory;
import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.rule.FactHandle;
import org.jboss.arquillian.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.seam.drools.KnowledgeBaseProducer;
import org.jboss.seam.drools.config.DroolsConfig;
import org.jboss.seam.drools.test.DroolsModuleFilter;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.weld.extensions.resources.ResourceProvider;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class KAgentTest
{
   @Deployment
   public static JavaArchive createTestArchive()
   {
      String pkgPath = KAgentTest.class.getPackage().getName().replaceAll("\\.", "/");
      JavaArchive archive = ShrinkWrap.create("test.jar", JavaArchive.class)
      .addPackages(true, new DroolsModuleFilter("kagent"), KnowledgeBaseProducer.class.getPackage())
      .addPackages(true, ResourceProvider.class.getPackage())
      .addClass(Person.class)
      .addClass(KAgentTestConfig.class)
      .addResource(pkgPath + "/kagenttestone.drl", ArchivePaths.create("kagenttestone.drl"))
      .addResource(pkgPath + "/kagenttesttwo.drl", ArchivePaths.create("kagenttesttwo.drl"))
      .addResource(pkgPath + "/kagenttestchangeset.xml", ArchivePaths.create("kagenttestchangeset.xml"))
      // .addResource(pkgPath + "/kbuilderconfig.properties",
            // ArchivePaths.create("kbuilderconfig.properties"))
            // .addResource(pkgPath + "/kbaseconfig.properties",
            // ArchivePaths.create("kbaseconfig.properties"))
            .addManifestResource(pkgPath + "/KAgentTest-beans.xml", ArchivePaths.create("beans.xml"));
      // System.out.println(archive.toString(Formatters.VERBOSE));
      return archive;
   }

   @Test
   public void testKAgentConfiguration(@KAgentTestConfig DroolsConfig config)
   {
      assertNotNull(config);
      assertTrue(config.isStartChangeNotifierService());
      assertTrue(config.isStartChangeScannerService());
      assertTrue(config.getAgentName() != null);
      assertTrue(config.getScannerInterval() > 0);
   }

   @Test
   public void testKAgent(@Default @KAgentTestConfig KnowledgeAgent agent, ResourceProvider resourceProvider)
   {
      assertNotNull(agent);
      assertNotNull(resourceProvider);
      Person p1 = new Person(19);
      StatefulKnowledgeSession ksession1 = agent.getKnowledgeBase().newStatefulKnowledgeSession();
      FactHandle fh1 = ksession1.insert(p1);
      ksession1.fireAllRules();
      Person p1f = (Person) ksession1.getObject(fh1);
      assertTrue(p1f.isEligible());
      ksession1.dispose();
      ResourceFactory.getResourceChangeNotifierService().stop();
      ResourceFactory.getResourceChangeScannerService().stop();
   }

}
