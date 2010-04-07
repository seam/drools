package org.jboss.seam.drools;

import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;

import org.jboss.weld.extensions.resources.ResourceProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author Tihomir Surdilovic
 */
public class QueryResultsProducer
{
   private static final Logger log = LoggerFactory.getLogger(KnowledgeBaseProducer.class);
   
   @Inject BeanManager manager;
   @Inject ResourceProvider resourceProvider;
}
