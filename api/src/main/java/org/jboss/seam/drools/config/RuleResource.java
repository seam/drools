/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc., and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.seam.drools.config;

import java.util.regex.Pattern;

import org.drools.io.ResourceFactory;

/**
 * @author stuart
 * @author tihomir
 */
public class RuleResource {
    public static final Pattern DIVIDER = Pattern.compile(":");

    private String fullPath;
    private String type; // ResourceType
    private String templateData;

    private String dtType; // DecisionTableInputType
    private String dtWorksheetName;

    private String location;
    private String resourcePath;

    public RuleResource() {
    }

    public RuleResource(String fullPath, String type) {
        this.fullPath = fullPath;
        this.type = type;
        splitFullPath();
    }

    public RuleResource(String fullPath, String type, String dtType, String dtWorksheetName) {
        this.fullPath = fullPath;
        this.type = type;
        this.dtType = dtType;
        this.dtWorksheetName = dtWorksheetName;
        splitFullPath();
    }

    public RuleResource(String fullPath, String type, String templateData) {
        this.fullPath = fullPath;
        this.type = type;
        this.templateData = templateData;
        splitFullPath();
    }

    private void splitFullPath() {
        String[] parts = DIVIDER.split(fullPath.trim());
        location = parts[0];
        resourcePath = parts[1];
    }

    public String getFullPath() {
        return fullPath;
    }

    public void setPath(String fullPath) {
        this.fullPath = fullPath;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTemplateData() {
        return templateData;
    }

    public void setTemplateData(String templateData) {
        this.templateData = templateData;
    }

    public String getDtType() {
        return dtType;
    }

    public void setDtType(String dtType) {
        this.dtType = dtType;
    }

    public String getDtWorksheetName() {
        return dtWorksheetName;
    }

    public void setDtWorksheetName(String dtWorksheetName) {
        this.dtWorksheetName = dtWorksheetName;
    }

    public org.drools.io.Resource getDroolsResouce() {
        if (location.equals("classpath")) {
            return ResourceFactory.newClassPathResource(resourcePath);
        } else if (location.equals("file")) {
            return ResourceFactory.newFileResource(resourcePath);
        } else if (location.equals("url")) {
            return ResourceFactory.newUrlResource(resourcePath);
        } else {
            return null;
        }
    }

}
