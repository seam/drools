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

import javax.enterprise.inject.Default;

import org.jboss.seam.drools.annotations.InsertFact;
import org.jboss.seam.drools.qualifiers.config.CEPPseudoClockConfig;
import org.jboss.seam.drools.qualifiers.config.DefaultConfig;

public class InterceptorsTestBean
{
   @InsertFact(fire=true) @Default @DefaultConfig
   public Person getPerson() {
      Person p = new Person();
      p.setEligible(false);
      p.setAge(22);
      return p;
   }
   
   @InsertFact(fire=true, entrypoint="peopleStream") @Default @CEPPseudoClockConfig
   public Person getPersonForEntryPoint() {
      Person p = new Person();
      p.setEligible(false);
      p.setAge(33);
      return p;
   }
}
