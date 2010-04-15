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
package org.jboss.seam.drools.test.ksession;

import org.drools.event.process.ProcessCompletedEvent;
import org.drools.event.process.ProcessEventListener;
import org.drools.event.process.ProcessNodeLeftEvent;
import org.drools.event.process.ProcessNodeTriggeredEvent;
import org.drools.event.process.ProcessStartedEvent;
import org.jboss.seam.drools.qualifiers.KSessionEventListener;

@KSessionEventListener
public class MyKnowledgeSessionEventListener implements ProcessEventListener
{

   public void afterNodeLeft(ProcessNodeLeftEvent arg0)
   {
      // TODO Auto-generated method stub
      
   }

   public void afterNodeTriggered(ProcessNodeTriggeredEvent arg0)
   {
      // TODO Auto-generated method stub
      
   }

   public void afterProcessCompleted(ProcessCompletedEvent arg0)
   {
      // TODO Auto-generated method stub
      
   }

   public void afterProcessStarted(ProcessStartedEvent arg0)
   {
      // TODO Auto-generated method stub
      
   }

   public void beforeNodeLeft(ProcessNodeLeftEvent arg0)
   {
      // TODO Auto-generated method stub
      
   }

   public void beforeNodeTriggered(ProcessNodeTriggeredEvent arg0)
   {
      // TODO Auto-generated method stub
      
   }

   public void beforeProcessCompleted(ProcessCompletedEvent arg0)
   {
      // TODO Auto-generated method stub
      
   }

   public void beforeProcessStarted(ProcessStartedEvent arg0)
   {
      // TODO Auto-generated method stub
      
   }
   
}
