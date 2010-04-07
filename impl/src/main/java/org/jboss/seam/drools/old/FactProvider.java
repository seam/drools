package org.jboss.seam.drools.old;

import java.util.List;
import java.util.Map;

/**
 * Interface for fact providers.
 * 
 * @author Tihomir Surdilovic
 * 
 */
public interface FactProvider
{
   public List<Object> getFacts();
   public void setFacts(List<Object> facts);
   
   public Map<String, Object> getGlobals();
   public void setGlobals(Map<String, Object> globals);
   
}