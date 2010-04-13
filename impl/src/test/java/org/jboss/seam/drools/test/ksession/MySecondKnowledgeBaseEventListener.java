package org.jboss.seam.drools.test.ksession;

import org.drools.event.knowledgebase.DefaultKnowledgeBaseEventListener;
import org.drools.event.knowledgebase.KnowledgeBaseEventListener;
import org.jboss.seam.drools.qualifiers.KBaseEventListener;

@KBaseEventListener
public class MySecondKnowledgeBaseEventListener extends DefaultKnowledgeBaseEventListener implements KnowledgeBaseEventListener
{
   public MySecondKnowledgeBaseEventListener() {
      super();
   }
}