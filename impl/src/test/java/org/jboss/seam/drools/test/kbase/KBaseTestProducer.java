package org.jboss.seam.drools.test.kbase;

import javax.enterprise.inject.Produces;

import org.jboss.seam.drools.KnowledgeBaseProducer;
import org.jboss.seam.drools.config.DroolsConfig;
import org.jboss.seam.drools.config.RuleResource;
import org.jboss.seam.drools.qualifiers.config.DefaultConfig;

public class KBaseTestProducer
{
   @DroolsConfig(knowledgeBuilderConfigProperties = "kbuilderconfig.properties", knowledgeBaseConfigProperties = "kbaseconfig.properties", ruleResources = { @RuleResource("classpath;kbasetest.drl;DRL;forkbasetest") })
   @Produces
   @DefaultConfig
   KnowledgeBaseProducer producer;
}
