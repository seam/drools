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
package org.jboss.seam.drools.test.cep;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotSame;

import java.util.concurrent.TimeUnit;

import javax.enterprise.inject.Default;
import javax.inject.Inject;

import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.rule.WorkingMemoryEntryPoint;
import org.drools.time.SessionPseudoClock;
import org.jboss.arquillian.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.seam.drools.KnowledgeBaseProducer;
import org.jboss.seam.drools.qualifiers.EntryPoint;
import org.jboss.seam.drools.qualifiers.config.CEPPseudoClockConfig;
import org.jboss.seam.drools.test.DroolsModuleFilter;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.weld.extensions.resources.ResourceProvider;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class CEPTest
{
   @Deployment
   public static JavaArchive createTestArchive()
   {
      String pkgPath = CEPTest.class.getPackage().getName().replaceAll("\\.", "/");
      JavaArchive archive = ShrinkWrap.create("test.jar", JavaArchive.class)
      .addPackages(true, new DroolsModuleFilter("cep"), KnowledgeBaseProducer.class.getPackage())
      .addPackages(true, ResourceProvider.class.getPackage())
      .addClass(FireAlarm.class)
      .addClass(FireDetected.class)
      .addClass(SprinklerActivated.class)
      .addResource(pkgPath + "/ceptest.drl", ArchivePaths.create("ceptest.drl"))
      //.addResource(pkgPath + "/kbuilderconfig.properties", ArchivePaths.create("kbuilderconfig.properties"))
      //.addResource(pkgPath + "/kbaseconfig.properties", ArchivePaths.create("kbaseconfig.properties"))
      .addManifestResource(pkgPath + "/CEPTest-beans.xml", ArchivePaths.create("beans.xml"));
      //System.out.println(archive.toString(Formatters.VERBOSE));
      return archive;
   }
   
   @Test
   public void dummy() {
      assertTrue(true);
   }
   
   //@Test
//   public void testCEP(@Default @CEPPseudoClockConfig StatefulKnowledgeSession cepSession,
//         @Default @CEPPseudoClockConfig @EntryPoint("FireDetectionStream") WorkingMemoryEntryPoint fireDetectionStream,
//         @Default @CEPPseudoClockConfig @EntryPoint("SprinklerDetectionStream") WorkingMemoryEntryPoint sprinklerDetectionStream) {
//      assertNotNull(cepSession);
//      assertTrue(cepSession.getId() >= 0);
//      assertNotNull(fireDetectionStream);
//      assertNotNull(sprinklerDetectionStream);
//      assertNotSame(fireDetectionStream, sprinklerDetectionStream);
//      
//        FireAlarm fireAlarm = new FireAlarm();
//        assertTrue(!fireAlarm.isActivated());
//        cepSession.setGlobal("fireAlarm", fireAlarm);
//        SessionPseudoClock clock = cepSession.getSessionClock();
//        fireDetectionStream.insert(new FireDetected());
//        clock.advanceTime(9, TimeUnit.SECONDS);
//        
//        cepSession.fireAllRules();
//        
//        FireAlarm afireAlarm = (FireAlarm) cepSession.getGlobal("fireAlarm");
//        assertTrue(!afireAlarm.isActivated());
//        
//        clock.advanceTime(2, TimeUnit.SECONDS);    
//        
//        cepSession.fireAllRules();
//        
//        FireAlarm bfireAlarm = (FireAlarm) cepSession.getGlobal("fireAlarm");
//        assertTrue(bfireAlarm.isActivated());
//      
//   }
}
