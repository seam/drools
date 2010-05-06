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
package org.jboss.seam.drools.config;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.drools.KnowledgeBaseConfiguration;
import org.drools.KnowledgeBaseFactory;
import org.drools.agent.KnowledgeAgentConfiguration;
import org.drools.agent.KnowledgeAgentFactory;
import org.drools.builder.KnowledgeBuilderConfiguration;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.io.ResourceChangeScannerConfiguration;
import org.drools.io.ResourceFactory;
import org.drools.runtime.KnowledgeSessionConfiguration;
import org.jboss.seam.drools.utils.ConfigUtils;
import org.jboss.weld.extensions.resourceLoader.ResourceProvider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Drools configuration file.
 * 
 * @author Tihomir Surdilovic
 */
public class DroolsConfig
{
   private static final Logger log = LoggerFactory.getLogger(DroolsConfig.class);

   @Inject
   ResourceProvider resourceProvider;

   private String knowledgeBuilderConfigProperties;
   private String knowledgeBaseConfigProperties;
   private String knowledgeSessionProperties;
   private String knowledgeAgentProperties;

   private boolean startChangeNotifierService;
   private boolean startChangeScannerService;
   private int scannerInterval = -1;
   private String agentName;

   private String loggerName;
   private String loggerType;
   private String loggerPath;
   private int loggerInterval;

   private boolean disableSeamDelegate;
   
   private RuleResources ruleResources;

   private Map<String, String> kbuilderPropertiesMap = new HashMap<String, String>();
   private Map<String, String> kbasePropertiesMap = new HashMap<String, String>();
   private Map<String, String> ksessionPropertiesMap = new HashMap<String, String>();
   private Map<String, String> kagentPropertiestMap = new HashMap<String, String>();

   public DroolsConfig()
   {
   }

   public DroolsConfig(RuleResources ruleResources)
   {
      this.ruleResources = ruleResources;
   }

   @PostConstruct
   public void setup()
   {
      readProperties(kbuilderPropertiesMap, knowledgeBuilderConfigProperties);
      readProperties(kbasePropertiesMap, knowledgeBaseConfigProperties);
      readProperties(ksessionPropertiesMap, knowledgeSessionProperties);
      readProperties(kagentPropertiestMap, knowledgeAgentProperties);

   }

   public ResourceChangeScannerConfiguration getResourceChangeScannerConfiguration()
   {
      ResourceChangeScannerConfiguration sconf = ResourceFactory.getResourceChangeScannerService().newResourceChangeScannerConfiguration();
      if (scannerInterval >= 0)
      {
         sconf.setProperty("drools.resource.scanner.interval", String.valueOf(scannerInterval));
      }
      return sconf;
   }

   public KnowledgeAgentConfiguration getKnowledgeAgentConfiguration()
   {
      KnowledgeAgentConfiguration config = KnowledgeAgentFactory.newKnowledgeAgentConfiguration();
      Iterator<Entry<String, String>> it = kagentPropertiestMap.entrySet().iterator();
      while (it.hasNext())
      {
         Map.Entry<String, String> nextExtry = it.next();
         config.setProperty(nextExtry.getKey(), nextExtry.getValue());
      }
      return config;
   }

   public KnowledgeSessionConfiguration getKnowledgeSessionConfiguration()
   {
      KnowledgeSessionConfiguration config = KnowledgeBaseFactory.newKnowledgeSessionConfiguration();
      Iterator<Entry<String, String>> it = ksessionPropertiesMap.entrySet().iterator();
      while (it.hasNext())
      {
         Map.Entry<String, String> nextExtry = it.next();
         config.setProperty(nextExtry.getKey(), nextExtry.getValue());
      }
      return config;
   }

   public KnowledgeBaseConfiguration getKnowledgeBaseConfiguration()
   {
      KnowledgeBaseConfiguration config = KnowledgeBaseFactory.newKnowledgeBaseConfiguration();
      Iterator<Entry<String, String>> it = kbasePropertiesMap.entrySet().iterator();
      while (it.hasNext())
      {
         Map.Entry<String, String> nextExtry = it.next();
         config.setProperty(nextExtry.getKey(), nextExtry.getValue());
      }
      return config;
   }

   public KnowledgeBuilderConfiguration getKnowledgeBuilderConfiguration()
   {
      KnowledgeBuilderConfiguration config = KnowledgeBuilderFactory.newKnowledgeBuilderConfiguration();
      Iterator<Entry<String, String>> it = kbuilderPropertiesMap.entrySet().iterator();
      while (it.hasNext())
      {
         Map.Entry<String, String> nextExtry = it.next();
         config.setProperty(nextExtry.getKey(), nextExtry.getValue());
      }
      return config;
   }

   private void readProperties(Map<String, String> propertiesMap, String propertiesPath)
   {
      if (propertiesPath != null)
      {
         try
         {
            Properties kbuilderProp = ConfigUtils.loadProperties(resourceProvider, propertiesPath);
            for (Object key : kbuilderProp.keySet())
            {
               propertiesMap.put((String) key, (String) kbuilderProp.get(key));
            }
         }
         catch (IOException e)
         {
            log.error("Unable to read configuration properties file: " + propertiesPath);
         }
      } else {
         log.debug("NULL properties path specified, bypassing reading properties");
      }
   }

   public String getKnowledgeBuilderConfigProperties()
   {
      return knowledgeBuilderConfigProperties;
   }

   public void setKnowledgeBuilderConfigProperties(String knowledgeBuilderConfigProperties)
   {
      this.knowledgeBuilderConfigProperties = knowledgeBuilderConfigProperties;
   }

   public String getKnowledgeBaseConfigProperties()
   {
      return knowledgeBaseConfigProperties;
   }

   public void setKnowledgeBaseConfigProperties(String knowledgeBaseConfigProperties)
   {
      this.knowledgeBaseConfigProperties = knowledgeBaseConfigProperties;
   }

   public String getKnowledgeSessionProperties()
   {
      return knowledgeSessionProperties;
   }

   public void setKnowledgeSessionProperties(String knowledgeSessionProperties)
   {
      this.knowledgeSessionProperties = knowledgeSessionProperties;
   }

   public String getKnowledgeAgentProperties()
   {
      return knowledgeAgentProperties;
   }

   public void setKnowledgeAgentProperties(String knowledgeAgentProperties)
   {
      this.knowledgeAgentProperties = knowledgeAgentProperties;
   }

   public boolean isStartChangeNotifierService()
   {
      return startChangeNotifierService;
   }

   public void setStartChangeNotifierService(boolean startChangeNotifierService)
   {
      this.startChangeNotifierService = startChangeNotifierService;
   }

   public boolean isStartChangeScannerService()
   {
      return startChangeScannerService;
   }

   public void setStartChangeScannerService(boolean startChangeScannerService)
   {
      this.startChangeScannerService = startChangeScannerService;
   }

   public int getScannerInterval()
   {
      return scannerInterval;
   }

   public void setScannerInterval(int scannerInterval)
   {
      this.scannerInterval = scannerInterval;
   }

   public String getAgentName()
   {
      return agentName;
   }

   public void setAgentName(String agentName)
   {
      this.agentName = agentName;
   }

   public String getLoggerName()
   {
      return loggerName;
   }

   public void setLoggerName(String loggerName)
   {
      this.loggerName = loggerName;
   }

   public String getLoggerType()
   {
      return loggerType;
   }

   public void setLoggerType(String loggerType)
   {
      this.loggerType = loggerType;
   }

   public String getLoggerPath()
   {
      return loggerPath;
   }

   public void setLoggerPath(String loggerPath)
   {
      this.loggerPath = loggerPath;
   }

   public int getLoggerInterval()
   {
      return loggerInterval;
   }

   public void setLoggerInterval(int loggerInterval)
   {
      this.loggerInterval = loggerInterval;
   }

   public RuleResources getRuleResources()
   {
      return ruleResources;
   }

   public void setRuleResources(RuleResources ruleResources)
   {
      this.ruleResources = ruleResources;
   }

   public boolean isDisableSeamDelegate()
   {
      return disableSeamDelegate;
   }

   public void setDisableSeamDelegate(boolean disableSeamDelegate)
   {
      this.disableSeamDelegate = disableSeamDelegate;
   }
   
   

}
