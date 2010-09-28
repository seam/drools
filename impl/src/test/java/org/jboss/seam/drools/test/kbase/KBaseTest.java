/*
 * JBoss, Home of Professional Open Source
 * Copyright ${year}, Red Hat, Inc., and individual contributors
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
package org.jboss.seam.drools.test.kbase;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.drools.KnowledgeBase;
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
public class KBaseTest
{
   @Deployment
   public static JavaArchive createTestArchive()
   {
      String pkgPath = KBaseTest.class.getPackage().getName().replaceAll("\\.", "/");
      JavaArchive archive = ShrinkWrap.create(JavaArchive.class)
      .addPackages(true, new DroolsModuleFilter("kbase"), KnowledgeBaseProducer.class.getPackage())
      .addClass(CreditRules.class)
      .addClass(DebitRules.class)
      .addClass(KBaseTestRuleResources.class)
      .addClass(MyKnowledgeBaseEventListener.class)
      .addResource(pkgPath + "/kbasetest.drl", ArchivePaths.create("kbasetest.drl"))
      .addResource(pkgPath + "/kbasetest.xls", ArchivePaths.create("kbasetest.xls"))
      .addResource(pkgPath + "/kbuilderconfig.properties", ArchivePaths.create("kbuilderconfig.properties"))
      .addResource(pkgPath + "/kbaseconfig.properties", ArchivePaths.create("kbaseconfig.properties"))
      .addManifestResource(pkgPath + "/KBaseTest-beans.xml", ArchivePaths.create("beans.xml"))
      .addManifestResource("META-INF/services/javax.enterprise.inject.spi.Extension", ArchivePaths.create("services/javax.enterprise.inject.spi.Extension"));
      //System.out.println(archive.toString(Formatters.VERBOSE));
      return archive;
   }
   
   @Test
   public void testKBase(@CreditRules KnowledgeBase ckbase, @DebitRules KnowledgeBase dkbase)
   {
      assertNotNull(ckbase);
      assertNotNull(dkbase);
      
      assertTrue(ckbase.getKnowledgePackages().size() == 1);
      assertTrue(ckbase.getKnowledgePackage("org.jboss.seam.drools.test.kbase").getRules().size() == 3);
      
      assertTrue(dkbase.getKnowledgePackages().size() == 1);
      assertTrue(dkbase.getKnowledgePackage("org.jboss.seam.drools.test.kbase").getRules().size() == 2);
   }
}
