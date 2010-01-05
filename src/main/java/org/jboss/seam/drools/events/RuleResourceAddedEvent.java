package org.jboss.seam.drools.events;

/**
 * This event is fires when a rule resource is added.
 *  
 * @author Tihomir Surdilovic
 */
public class RuleResourceAddedEvent
{
   String resource;

   public RuleResourceAddedEvent(String resource)
   {
      this.resource = resource;
   }

   public String getResource()
   {
      return resource;
   }

   public void setResource(String resource)
   {
      this.resource = resource;
   }
}
