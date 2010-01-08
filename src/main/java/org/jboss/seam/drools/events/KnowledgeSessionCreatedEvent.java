package org.jboss.seam.drools.events;

/**
 * This event is fires when Stateful or Stateless KnowledgeSession is created.
 *  
 * @author Tihomir Surdilovic
 */
public class KnowledgeSessionCreatedEvent
{
   private int sessionId;
   
   public KnowledgeSessionCreatedEvent(int sessionId) {
      this.sessionId = sessionId;
   }

   public int getSessionId()
   {
      return sessionId;
   }

   public void setSessionId(int sessionId)
   {
      this.sessionId = sessionId;
   }
   
}
