package org.jboss.seam.drools.utils;

import java.util.regex.Pattern;

/**
 * 
 * @author Tihomir Surdilovic
 */
public class ConfigUtils
{
   private static final Pattern DIVIDER = Pattern.compile(":");
   // KBase config
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

   // KSession config
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

   // KAgent config

}
