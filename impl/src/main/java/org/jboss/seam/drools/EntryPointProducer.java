package org.jboss.seam.drools;

import javax.enterprise.inject.Instance;
import javax.enterprise.inject.Produces;

import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.rule.WorkingMemoryEntryPoint;
import org.jboss.seam.drools.qualifiers.KAgentConfigured;
import org.jboss.seam.drools.qualifiers.KBaseConfigured;

/**
 * 
 * @author Tihomir Surdilovic
 */
public class EntryPointProducer
{
   @Produces @KBaseConfigured WorkingMemoryEntryPoint produceEntryPoint(@KBaseConfigured StatefulKnowledgeSession ksession, Instance<String> entryPointNameInstance) {
      return ksession.getWorkingMemoryEntryPoint(entryPointNameInstance.get());
   }
   
   @Produces @KAgentConfigured WorkingMemoryEntryPoint produceKAgentEntryPoint(@KAgentConfigured StatefulKnowledgeSession ksession, Instance<String> entryPointNameInstance) {
      return ksession.getWorkingMemoryEntryPoint(entryPointNameInstance.get());
   }
   
}
