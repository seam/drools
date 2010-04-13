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
