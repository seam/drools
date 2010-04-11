package org.jboss.seam.drools.config;

public class KnowledgeLoggerConfig
{
   private String name;
   private String type;
   private String path;
   private int interval;

   public boolean isFileType()
   {
      return type != null && type.equalsIgnoreCase("file");
   }

   public boolean isConsoleType()
   {
      return type != null && type.equalsIgnoreCase("console");
   }

   public boolean isThreadedType()
   {
      return type != null && type.equalsIgnoreCase("threaded");
   }

   public String getName()
   {
      return name;
   }

   public void setName(String name)
   {
      this.name = name;
   }

   public String getType()
   {
      return type;
   }

   public void setType(String type)
   {
      this.type = type;
   }

   public String getPath()
   {
      return path;
   }

   public void setPath(String path)
   {
      this.path = path;
   }

   public int getInterval()
   {
      return interval;
   }

   public void setInterval(int interval)
   {
      this.interval = interval;
   }

}
