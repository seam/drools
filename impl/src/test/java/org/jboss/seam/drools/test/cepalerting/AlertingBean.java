package org.jboss.seam.drools.test.cepalerting;

import org.jboss.seam.drools.annotations.cep.Event;
import org.jboss.seam.drools.annotations.cep.Max;
import org.jboss.seam.drools.annotations.cep.OverWindowTime;

public class AlertingBean {
    @Event
    @Max(3)
    @OverWindowTime("10m")
    public void doSomething(String something) {
        // guess do nothing
    }
}
