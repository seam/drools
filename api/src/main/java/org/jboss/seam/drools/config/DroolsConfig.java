package org.jboss.seam.drools.config;

/**
 * Drools configuration parameters 
 * 
 * @author Shane Bryzak
 */
public class DroolsConfig
{
   private String kbuilderConfigFile;
   private String kbaseConfigFile;
   private String ksessionConfigFile;
   private String kagentConfigFile;
   private String serializationSigningConfigFile;

   private boolean startChangeNotifierService;
   private boolean startChangeScannerService;
   private int scannerInterval = -1;
   private String agentName;

   private String loggerName;
   private String loggerType;
   private String loggerPath;
   private int loggerInterval = 0;

   private boolean disableSeamDelegate;
   
   private DroolsProperty[] kbuilderProperties = {};

   private DroolsProperty[] kbaseProperties = {};

   private DroolsProperty[] ksessionProperties = {};

   private DroolsProperty[] kagentProperties = {};
   
   private DroolsProperty[] serializationSigningProperties = {};
   
   public String getKBuilderConfigFile()
   {
      return kbuilderConfigFile;
   }
   
   public String getKBaseConfigFile()
   {
      return kbaseConfigFile;
   }
   
   public String getKSessionConfigFile()
   {
      return ksessionConfigFile;
   }
   
   public String getKAgentConfigFile()
   {
      return kagentConfigFile;
   }
   
   public String getSerializationSigningConfigFile()
   {
      return serializationSigningConfigFile;
   }
   
   public boolean getStartChangeNotifierService()
   {
      return startChangeNotifierService;
   }
   
   public boolean getStartChangeScannerService()
   {
      return startChangeScannerService;
   }
   
   public int getScannerInterval()
   {
      return scannerInterval;
   }
   
   public String getAgentName()
   {
      return agentName;
   }
   
   public String getLoggerName()
   {
      return loggerName;
   }
   
   public String getLoggerType()
   {
      return loggerType;
   }
   
   public String getLoggerPath()
   {
      return loggerPath;
   }
   
   public int getLoggerInterval()
   {
      return loggerInterval;
   }
     
   public boolean getDisableSeamDelegate()
   {
      return disableSeamDelegate;
   }
   
   public DroolsProperty[] getKBuilderProperties()
   {
      return kbuilderProperties;
   }

   public DroolsProperty[] getKBaseProperties() 
   {
      return kbaseProperties;
   }

   public DroolsProperty[] getKSessionProperties()
   {
      return ksessionProperties;
   }

   public DroolsProperty[] getKAgentProperties()
   {
      return kagentProperties;
   }
   
   public DroolsProperty[] getSerializationSigningProperties()
   {
      return serializationSigningProperties;
   }
   
   public DroolsConfig setKBuilderConfigFile(String fileName)
   {
      this.kbuilderConfigFile = fileName;
      return this;
   }
   
   public DroolsConfig setKBaseConfigFile(String fileName)
   {
      this.kbaseConfigFile = fileName;
      return this;
   }
   
   public DroolsConfig setKSessionConfigFile(String fileName)
   {
      this.ksessionConfigFile = fileName;
      return this;
   }
   
   public DroolsConfig setKAgentConfigFile(String fileName)
   {
      this.kagentConfigFile = fileName;
      return this;
   }
   
   public DroolsConfig setSerializationSigningConfigFile(String fileName)
   {
      this.serializationSigningConfigFile = fileName;
      return this;
   }
   
   public DroolsConfig setStartChangeNotifierService(boolean value)
   {
      this.startChangeNotifierService = value;
      return this;
   }
   
   public DroolsConfig setStartChangeScannerService(boolean value)
   {
      this.startChangeScannerService = value;
      return this;
   }
   
   public DroolsConfig setScannerInterval(int interval)
   {
      this.scannerInterval = interval;
      return this;
   }
   
   public DroolsConfig setAgentName(String agentName)
   {
      this.agentName = agentName;
      return this;
   }
   
   public DroolsConfig setLoggerName(String loggerName)
   {
      this.loggerName = loggerName;
      return this;
   }
   
   public DroolsConfig setLoggerType(String loggerType)
   {
      this.loggerType = loggerType;
      return this;
   }
   
   public DroolsConfig setLoggerPath(String loggerPath)
   {
      this.loggerPath = loggerPath;
      return this;
   }
   
   public DroolsConfig setLoggerInterval(int loggerInterval)
   {
      this.loggerInterval = loggerInterval;
      return this;
   }
   
   public DroolsConfig setDisableSeamDelegate(boolean value)
   {
      this.disableSeamDelegate = value;
      return this;
   }
   
   public DroolsConfig setKBuilderProperties(DroolsProperty[] properties)
   {
      this.kbuilderProperties = properties;
      return this;
   }
   
   public DroolsConfig setKBaseProperties(DroolsProperty[] properties)
   {
      this.kbaseProperties = properties;
      return this;
   }
   
   public DroolsConfig setKSessionProperties(DroolsProperty[] properties)
   {
      this.ksessionProperties = properties;
      return this;
   }
   
   public DroolsConfig setKAgentProperties(DroolsProperty[] properties)
   {
      this.kagentProperties = properties;
      return this;
   }
   
   public DroolsConfig setSerializationSigningProperties(DroolsProperty[] properties)
   {
      this.serializationSigningProperties = properties;
      return this;
   }
}
