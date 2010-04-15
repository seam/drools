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
package org.jboss.seam.drools.test.kbase;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.jboss.seam.drools.TemplateDataProvider;
import org.jboss.seam.drools.qualifiers.TemplateData;

@TemplateData("forkbasetest")
public class SimpleTemplateDataProvider implements TemplateDataProvider
{
   private static Collection<Map<String, Object>> templateData = new ArrayList<Map<String,Object>>();
   static {
      Map<String, Object> m1 = new HashMap<String, Object>();
      m1.put("name", "Tihomir");
      templateData.add(m1);
      Map<String, Object> m2 = new HashMap<String, Object>();
      m2.put("name", "Stuart");
      templateData.add(m2);
      Map<String, Object> m3 = new HashMap<String, Object>();
      m3.put("name", "Dan");
      templateData.add(m3);
   }
   public Collection<Map<String, Object>> getTemplateData()
   {
      return templateData;
   }
   
}
