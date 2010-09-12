package org.jboss.seam.drools.test.channel;

import java.util.ArrayList;
import java.util.List;

import org.jboss.seam.drools.qualifiers.Channel;

@Channel("testChannel")
public class ChannelBean implements org.drools.runtime.Channel
{
   private int numOfSentObjects = 0;
   private List<Person> eligiblesList = new ArrayList<Person>();
   private List<Person> notEligiblesList = new ArrayList<Person>();
   
   public void send(Object personObject)
   {
      Person p = (Person) personObject;
      if(p.isEligible()) {
         eligiblesList.add(p);
      } else {
         notEligiblesList.add(p);
      }
      numOfSentObjects++;
   }  
   
   public int getNumOfSentObjects() {
      return numOfSentObjects;
   }
   
   public List<Person> getEligiblesList() {
      return eligiblesList;
   }
   
   public List<Person> getNotEligiblesList() {
      return notEligiblesList;
   }
}
