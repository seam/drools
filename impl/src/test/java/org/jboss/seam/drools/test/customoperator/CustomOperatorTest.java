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
package org.jboss.seam.drools.test.customoperator;

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
public class CustomOperatorTest
{
   @Deployment
   public static JavaArchive createTestArchive()
   {
      String pkgPath = CustomOperatorTest.class.getPackage().getName().replaceAll("\\.", "/");
      JavaArchive archive = ShrinkWrap.create(JavaArchive.class)
      /**.addPackages(true, new DroolsModuleFilter("customoperator"), KnowledgeBaseProducer.class.getPackage())
      .addPackages(true, ResourceProvider.class.getPackage())
      .addClass(MessageBean.class)
      .addClass(StrEvaluator.class)
      .addClass(StrEvaluatorDefinition.class)
      .addResource(pkgPath + "/customoperatortest.drl", ArchivePaths.create("customoperatortest.drl"))
      // .addResource(pkgPath + "/kbuilderconfig.properties",
      // ArchivePaths.create("kbuilderconfig.properties"))
      // .addResource(pkgPath + "/kbaseconfig.properties",
      // ArchivePaths.create("kbaseconfig.properties"))
      .addManifestResource(pkgPath + "/CustomOperatorTest-beans.xml", ArchivePaths.create("beans.xml"))
      .addManifestResource("META-INF/services/javax.enterprise.inject.spi.Extension", ArchivePaths.create("services/javax.enterprise.inject.spi.Extension"));
      // System.out.println(archive.toString(Formatters.VERBOSE))**/;
      return archive;
   }
   /**

   @Inject
   @Default
   @DefaultConfig
   StatefulKnowledgeSession ksession;

   @Test
   public void testCustomOperator()
   {
      assertNotNull(ksession);
      MessageBean mb = new MessageBean();
      mb.setRoutingValue("R1:messageBody:R2");
      org.drools.runtime.rule.FactHandle mbHandle = ksession.insert(mb);
      ksession.fireAllRules();
      MessageBean mbAfterEval = (MessageBean) ksession.getObject(mbHandle);
      assertTrue(mbAfterEval.getResult().equals("Message starts with R1, ends with R2 and it's length is 17"));
   }**/
   @Test
   public void nothingToTest() {
      
   }
}
