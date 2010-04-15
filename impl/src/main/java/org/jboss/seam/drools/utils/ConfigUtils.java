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
package org.jboss.seam.drools.utils;

import java.io.InputStream;
import java.util.Properties;
import java.util.regex.Pattern;

import org.jboss.weld.extensions.resources.ResourceProvider;

/**
 * 
 * @author Tihomir Surdilovic
 */
public class ConfigUtils
{
   private static final Pattern DIVIDER = Pattern.compile(";");

   private static final int RESOURCE_PATH = 0;
   private static final int RESOURCE = 1;
   private static final int RESOURCE_TYPE = 2;
   private static final int RESOURCE_TEMPLATE_DATA = 3;

   public static final String RESOURCE_TYPE_URL = "url";
   public static final String RESOURCE_TYPE_FILE = "file";
   public static final String RESOURCE_TYPE_CLASSPATH = "classpath";

   public static boolean isValidResource(String resource)
   {
      return DIVIDER.split(resource.trim()).length >= 3;
   }

   public static boolean isRuleTemplate(String resource)
   {
      return DIVIDER.split(resource.trim()).length == 4;
   }

   public static String getResourceType(String resource)
   {
      return DIVIDER.split(resource.trim())[RESOURCE_TYPE];
   }

   public static String getRuleResource(String resource)
   {
      return DIVIDER.split(resource.trim())[RESOURCE];
   }

   public static String getTemplateData(String resource)
   {
      return DIVIDER.split(resource.trim())[RESOURCE_TEMPLATE_DATA];
   }

   public static String getResourcePath(String resource)
   {
      return DIVIDER.split(resource.trim())[RESOURCE_PATH];
   }

   private static final int WORKITEMHANDLER_NAME = 0;
   private static final int WORKITEMHANDLER_TYPE = 1;

   public static String getWorkItemHandlerName(String workItemHandlerStr)
   {
      return DIVIDER.split(workItemHandlerStr.trim())[WORKITEMHANDLER_NAME];
   }

   public static String getWorkItemHandlerType(String workItemHandlerStr)
   {
      return DIVIDER.split(workItemHandlerStr.trim())[WORKITEMHANDLER_TYPE];
   }

   public static boolean isValidWorkItemHandler(String workItemHandlerStr)
   {
      return DIVIDER.split(workItemHandlerStr.trim()).length == 2;
   }

   public static Properties loadProperties(ResourceProvider resourceProvider, String path) throws Exception
   {
      Properties prop = new Properties();
      InputStream in = resourceProvider.loadResourceStream(path);
      if (in == null)
      {
         throw new IllegalStateException("Could not locate: " + path);
      }
      prop.load(in);
      in.close();
      return prop;
   }

}
