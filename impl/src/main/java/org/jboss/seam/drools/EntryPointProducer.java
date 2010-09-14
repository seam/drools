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
package org.jboss.seam.drools;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;

import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.rule.WorkingMemoryEntryPoint;
import org.jboss.seam.drools.config.DroolsConfig;
import org.jboss.seam.drools.qualifiers.EntryPoint;
import org.jboss.seam.drools.qualifiers.Scanned;
import org.jboss.weld.extensions.bean.generic.Generic;
import org.jboss.weld.extensions.bean.generic.GenericProduct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author Tihomir Surdilovic
 */
@SessionScoped
@Generic(DroolsConfig.class)
public class EntryPointProducer implements Serializable
{
   private static final Logger log = LoggerFactory.getLogger(EntryPointProducer.class);

   @Inject
   @GenericProduct
   StatefulKnowledgeSession statefullKsession;
   
   @Inject
   @Scanned
   @GenericProduct
   StatefulKnowledgeSession statefulKnowledgeSession;

   @Produces
   @Default
   @EntryPoint
   public WorkingMemoryEntryPoint produceEntryPoint(InjectionPoint ip) throws Exception
   {
      String entryPointName = ip.getAnnotated().getAnnotation(EntryPoint.class).value();
      if (entryPointName != null && entryPointName.length() > 0)
      {
         log.debug("EntryPoint Name requested: " + entryPointName);
         WorkingMemoryEntryPoint entryPoint = statefullKsession.getWorkingMemoryEntryPoint( entryPointName );
         return entryPoint;
      }
      else
      {
         throw new IllegalStateException("EntryPoint must have a name.");
      }
   }

   @Produces
   @Scanned
   @EntryPoint
   public WorkingMemoryEntryPoint produceScannedEntryPoint(InjectionPoint ip) throws Exception
   {
      String entryPointName = ip.getAnnotated().getAnnotation(EntryPoint.class).value();
      if (entryPointName != null && entryPointName.length() > 0)
      {
         log.debug("EntryPoint Name requested: " + entryPointName);
         return statefulKnowledgeSession.getWorkingMemoryEntryPoint(entryPointName);
      }
      else
      {
         throw new IllegalStateException("EntryPoint must have a name.");
      }
   }

}
