package org.jboss.seam.drools;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;

import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.rule.WorkingMemoryEntryPoint;
import org.jboss.seam.drools.qualifiers.EntryPoint;
import org.jboss.seam.drools.qualifiers.Scanned;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author Tihomir Surdilovic
 */
public class EntryPointProducer
{
   private static final Logger log = LoggerFactory.getLogger(EntryPointProducer.class);

   @Produces
   @EntryPoint
   public WorkingMemoryEntryPoint produceEntryPoint(StatefulKnowledgeSession ksession, InjectionPoint ip) throws Exception
   {
      String entryPointName = ip.getAnnotated().getAnnotation(EntryPoint.class).value();
      if (entryPointName != null && entryPointName.length() > 0)
      {
         log.debug("EntryPoint Name requested: " + entryPointName);
         return ksession.getWorkingMemoryEntryPoint(entryPointName);
      }
      else
      {
         throw new IllegalStateException("EntryPoint must have a name.");
      }
   }

   @Produces
   @EntryPoint
   @Scanned
   public WorkingMemoryEntryPoint produceScannedEntryPoint(@Scanned StatefulKnowledgeSession ksession, InjectionPoint ip) throws Exception
   {
      String entryPointName = ip.getAnnotated().getAnnotation(EntryPoint.class).value();
      if (entryPointName != null && entryPointName.length() > 0)
      {
         log.debug("EntryPoint Name requested: " + entryPointName);
         return ksession.getWorkingMemoryEntryPoint(ip.getAnnotated().getAnnotation(EntryPoint.class).value());
      }
      else
      {
         throw new IllegalStateException("EntryPoint must have a name.");
      }
   }

}
