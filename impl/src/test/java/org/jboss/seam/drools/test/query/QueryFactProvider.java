package org.jboss.seam.drools.test.query;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jboss.seam.drools.FactProvider;

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

public class QueryFactProvider implements FactProvider {

    public List<Object> getFacts() {
        List<Object> personList = new ArrayList<Object>();
        personList.add(new Person(10));
        personList.add(new Person(17));
        personList.add(new Person(16));
        personList.add(new Person(13));
        personList.add(new Person(33));
        personList.add(new Person(22));
        personList.add(new Person(54));
        personList.add(new Person(7));
        personList.add(new Person(32));
        personList.add(new Person(12));
        personList.add(new Person(67));
        personList.add(new Person(55));
        personList.add(new Person(77));
        personList.add(new Person(13));

        return personList;
    }

    public Map<String, Object> getGlobals() {
        return null;
    }

    public void setFacts(List<Object> facts) {

    }

    public void setGlobals(Map<String, Object> globals) {

    }

    public List<String> getQueries() {
        List<String> queries = new ArrayList<String>();
        queries.add("number of adults");
        queries.add("number of minors");
        return queries;
    }

    public void setQueries(List<String> queries) {

    }

    public String getBatchXml() {
        return null;
    }

    public void setBatchXml(String batchXml) {

    }


}
