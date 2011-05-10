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
package org.jboss.seam.drools.test.sessionreport;

import org.jboss.arquillian.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.seam.drools.test.query.QueryTest;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class SessionReportTest {
    @Deployment
    public static JavaArchive createTestArchive() {
        String pkgPath = QueryTest.class.getPackage().getName().replaceAll("\\.", "/");
        JavaArchive archive = ShrinkWrap.create(JavaArchive.class)
                /**.addPackages(true, new DroolsModuleFilter("sessionreport"), KnowledgeBaseProducer.class.getPackage())
                 .addPackages(true, ResourceProvider.class.getPackage())
                 .addClass(Cheese.class)
                 .addResource(pkgPath + "/sessionreporttest.drl", ArchivePaths.create("sessionreporttest.drl"))
                 //.addResource(pkgPath + "/kbuilderconfig.properties", ArchivePaths.create("kbuilderconfig.properties"))
                 //.addResource(pkgPath + "/kbaseconfig.properties", ArchivePaths.create("kbaseconfig.properties"))
                 .addManifestResource(pkgPath + "/SessionReportTest-beans.xml", ArchivePaths.create("beans.xml"))
                 .addManifestResource("META-INF/services/javax.enterprise.inject.spi.Extension", ArchivePaths.create("services/javax.enterprise.inject.spi.Extension"));
                 //System.out.println(archive.toString(Formatters.VERBOSE))**/;
        return archive;
    }

    /**
     * @Inject @Default @DefaultConfig StatefulKnowledgeSession ksession;
     * @Before public void setupKSession() {
     * assertNotNull(ksession);
     * Cheese c1 = new Cheese("stillton", 10);
     * Cheese c2 = new Cheese("stillton", 2);
     * Cheese c3 = new Cheese("stillton", 4);
     * Cheese c4 = new Cheese("stillton", 44);
     * Cheese c5 = new Cheese("stillton", 23);
     * Cheese c6 = new Cheese("stillton", 1);
     * Cheese c7 = new Cheese("stillton", 7);
     * Cheese c8 = new Cheese("stillton", 4);
     * Cheese c9 = new Cheese("mozarella", 77);
     * Cheese c10 = new Cheese("mozarella", 53);
     * Cheese c11 = new Cheese("mozarella", 31);
     * Cheese c12 = new Cheese("mozarella", 7);
     * Cheese c13 = new Cheese("mozarella", 3);
     * <p/>
     * ksession.insert(c1);
     * ksession.insert(c2);
     * ksession.insert(c3);
     * ksession.insert(c4);
     * ksession.insert(c5);
     * ksession.insert(c6);
     * ksession.insert(c7);
     * ksession.insert(c8);
     * ksession.insert(c9);
     * ksession.insert(c10);
     * ksession.insert(c11);
     * ksession.insert(c12);
     * ksession.insert(c13);
     * <p/>
     * ksession.fireAllRules();
     * }
     * @Test public void testGeneratedReport(@Default @DefaultConfig @SessionReport SessionReportWrapper wrapper) {
     * assertNotNull(wrapper);
     * assertNotNull(wrapper.getReport());
     * }*
     */
    @Test
    public void nothingToTest() {

    }
}
