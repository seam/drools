package org.jboss.seam.drools.insertion;

import org.jboss.seam.drools.annotation.InsertFact;

public class InsertionBean
{
   @InsertFact(ksessionId=123)
   public String insertResultAsFact() {
      return "abc";
   }
}
