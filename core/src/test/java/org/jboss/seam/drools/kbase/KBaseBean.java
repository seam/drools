package org.jboss.seam.drools.kbase;

import org.drools.KnowledgeBase;
import org.jboss.seam.drools.config.KnowledgeBaseConfig;

public class KBaseBean
{
   private KnowledgeBaseConfig kbaseconfig;
   private KnowledgeBase kbase;
   public KnowledgeBaseConfig getKbaseconfig()
   {
      return kbaseconfig;
   }
   public void setKbaseconfig(KnowledgeBaseConfig kbaseconfig)
   {
      this.kbaseconfig = kbaseconfig;
   }
   public KnowledgeBase getKbase()
   {
      return kbase;
   }
   public void setKbase(KnowledgeBase kbase)
   {
      this.kbase = kbase;
   }
   
   
}
