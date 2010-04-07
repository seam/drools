package org.jboss.seam.drools;

import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author Tihomir Surdilovic
 */
public class KnowledgeAgentProducer
{
   private static final Logger log = LoggerFactory.getLogger(KnowledgeBaseProducer.class);
   
   @Inject BeanManager manager;
}
