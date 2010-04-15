package org.jboss.seam.drools;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;

import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.rule.QueryResults;
import org.jboss.seam.drools.qualifiers.Query;
import org.jboss.seam.drools.qualifiers.Scanned;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author Tihomir Surdilovic
 */
public class QueryResultsProducer
{
   private static final Logger log = LoggerFactory.getLogger(QueryResultsProducer.class);

   @Produces
   @Query
   public QueryResults produceQueryResults(StatefulKnowledgeSession ksession, InjectionPoint ip)
   {
      String queryName = ip.getAnnotated().getAnnotation(Query.class).value();
      if (queryName != null && queryName.length() > 0)
      {
         log.debug("Query Name requested: " + queryName);
         return ksession.getQueryResults(queryName);
      }
      else
      {
         throw new IllegalStateException("Query must have a name.");
      }
   }
   
   @Produces
   @Query
   @Scanned
   public QueryResults produceScannedQueryResults(@Scanned StatefulKnowledgeSession ksession, InjectionPoint ip) {
      String queryName = ip.getAnnotated().getAnnotation(Query.class).value();
      if (queryName != null && queryName.length() > 0)
      {
         log.debug("Query Name requested: " + queryName);
         return ksession.getQueryResults(queryName);
      }
      else
      {
         throw new IllegalStateException("Query must have a name.");
      }
   }
}
