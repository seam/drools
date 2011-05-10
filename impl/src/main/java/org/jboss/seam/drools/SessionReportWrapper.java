package org.jboss.seam.drools;

import javax.enterprise.context.Dependent;

@Dependent
public class SessionReportWrapper {
    private String report;

    public String getReport() {
        return report;
    }

    public void setReport(String report) {
        this.report = report;
    }
}
