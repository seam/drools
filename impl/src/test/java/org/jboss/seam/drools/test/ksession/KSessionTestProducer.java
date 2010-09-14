package org.jboss.seam.drools.test.ksession;

import javax.enterprise.inject.Produces;

import org.jboss.seam.drools.KnowledgeBaseProducer;
import org.jboss.seam.drools.KnowledgeSessionProducer;
import org.jboss.seam.drools.config.DroolsConfig;
import org.jboss.seam.drools.config.RuleResource;
import org.jboss.seam.drools.qualifiers.config.DefaultConfig;
import org.jboss.seam.drools.qualifiers.config.MVELDialectConfig;

public class KSessionTestProducer
{
   @DroolsConfig(knowledgeBuilderConfigProperties = "kbuilderconfig.properties", knowledgeBaseConfigProperties = "kbaseconfig.properties", ruleResources = { @RuleResource("classpath;ksessiontest.drl;DRL") })
   @Produces
   @DefaultConfig
   KnowledgeSessionProducer defaultProducer;
   
   //@DroolsConfig(knowledgeBuilderConfigProperties = "kbuilderconfig.properties", knowledgeBaseConfigProperties = "kbaseconfig.properties", ruleResources = { @RuleResource("classpath;ksessiontest2.drl;DRL") })
   //@Produces
   //@MVELDialectConfig
   //KnowledgeBaseProducer mvelDialectProducer;
}
