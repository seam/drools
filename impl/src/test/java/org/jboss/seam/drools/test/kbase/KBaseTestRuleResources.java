package org.jboss.seam.drools.test.kbase;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

import org.jboss.seam.drools.config.Drools;
import org.jboss.seam.drools.config.DroolsProperty;
import org.jboss.seam.drools.config.RuleResource;
import org.jboss.seam.drools.config.RuleResources;

public class KBaseTestRuleResources {
    @Drools(kbuilderConfigFile = "kbuilderconfig.properties", kbaseConfigFile = "kbaseconfig.properties")
    @Produces
    @ApplicationScoped
    @CreditRules
    public RuleResources configureCreditRules() {
        return new RuleResources().add(new RuleResource("classpath:kbasetest.drl", "DRL", "forkbasetest"));
    }

    @Drools(kbuilderProperties = {@DroolsProperty(name = "drools.dialect.default", value = "java")})
    @Produces
    @ApplicationScoped
    @DebitRules
    public RuleResources configureDebitRules() {
        return new RuleResources().add(new RuleResource("classpath:kbasetest.xls", "DTABLE", "XLS", "Tables_2"));
    }
}
