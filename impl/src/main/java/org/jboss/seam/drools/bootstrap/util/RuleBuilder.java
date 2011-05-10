package org.jboss.seam.drools.bootstrap.util;

import java.util.List;

/*
 * JBoss, Home of Professional Open Source
 * Copyright ${year}, Red Hat, Inc., and individual contributors
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
public class RuleBuilder {
    private static final String rulePackage = "org.jboss.seam.drools.cepvalidation";

    private List<String> imports;
    private List<DeclaredType> declaredTypes;
    private List<Rule> rules;


    public List<String> getImports() {
        return imports;
    }

    public void setImports(List<String> imports) {
        this.imports = imports;
    }

    public List<DeclaredType> getDeclaredTypes() {
        return declaredTypes;
    }

    public void setDeclaredTypes(List<DeclaredType> declaredTypes) {
        this.declaredTypes = declaredTypes;
    }

    public List<Rule> getRules() {
        return rules;
    }

    public void setRules(List<Rule> rules) {
        this.rules = rules;
    }

    public static String getRulepackage() {
        return rulePackage;
    }


    public String getRuleDrl() {
        return null; // for now
    }


}
