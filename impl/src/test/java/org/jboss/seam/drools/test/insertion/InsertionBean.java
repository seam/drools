package org.jboss.seam.drools.test.insertion;

import org.jboss.seam.drools.annotations.InsertFact;

public class InsertionBean
{
   @InsertFact(ksessionId = 123)
   public String insertResultAsFact()
   {
      return "abc";
   }
}
