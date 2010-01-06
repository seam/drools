package org.jboss.seam.drools;

import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;

import org.drools.KnowledgeBase;
import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.StatelessKnowledgeSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Manager component for a Drools Knowledge Sessions.
 * 
 * @author Tihomir Surdilovic
 */
@Dependent
public class KnowledgeSessionManager
{
   private static final Logger log = LoggerFactory.getLogger(KnowledgeSessionManager.class);

   @Inject
   BeanManager manager;
   
   @Inject 
   KnowledgeBase kbase;
   
   @Produces
   public StatefulKnowledgeSession getStatefulSession() {
      return null; // for now
   }
   
   @Produces
   public StatelessKnowledgeSession getStatelessSession() {
      return null; // for now
   }
}
