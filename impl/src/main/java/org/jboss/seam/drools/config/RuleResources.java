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

/**
 * 
 * @author Tihomir Surdilovic
 */
public class RuleResources
{
   public static final Pattern DIVIDER = Pattern.compile(";");
   public static final int LOCATION_TYPE = 0;
   public static final int RESOURCE_PATH = 1;
   public static final int RESOURCE_TYPE = 2;
   public static final int TEMPLATE_DATAPROVIDER_NAME = 3;
   
   public static final String LOCATION_TYPE_URL = "url";
   public static final String LOCATION_TYPE_FILE = "file";
   public static final String LOCATION_TYPE_CLASSPATH = "classpath";

   
   private String[] resources;

   public String[] getResources()
   {
      return resources;
   }

   public void setResources(String[] resources)
   {
      this.resources = resources;
   }
   
}
