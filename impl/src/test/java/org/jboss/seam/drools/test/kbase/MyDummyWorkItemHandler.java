package org.jboss.seam.drools.test.kbase;

import org.drools.runtime.process.WorkItem;
import org.drools.runtime.process.WorkItemHandler;
import org.drools.runtime.process.WorkItemManager;
import org.jboss.seam.drools.qualifiers.WIHandler;

@WIHandler(name="dummy1")
public class MyDummyWorkItemHandler implements WorkItemHandler
{

   public void abortWorkItem(WorkItem arg0, WorkItemManager arg1)
   {
      // TODO Auto-generated method stub
      
   }

   public void executeWorkItem(WorkItem arg0, WorkItemManager arg1)
   {
      // TODO Auto-generated method stub
      
   }

}
