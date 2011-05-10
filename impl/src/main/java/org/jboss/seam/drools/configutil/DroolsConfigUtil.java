package org.jboss.seam.drools.configutil;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
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
import org.jboss.seam.drools.config.Drools;
import org.jboss.seam.drools.config.DroolsProperty;
import org.jboss.seam.drools.utils.ConfigUtils;
import org.jboss.seam.solder.bean.generic.Generic;
import org.jboss.seam.solder.resourceLoader.ResourceProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Generic
@ApplicationScoped
public class DroolsConfigUtil implements Serializable {
    private static final Logger log = LoggerFactory.getLogger(DroolsConfigUtil.class);

    @Inject
    ResourceProvider resourceProvider;

    @Inject
    Drools config;

    private final Map<String, String> kbuilderPropertiesMap = new HashMap<String, String>();
    private final Map<String, String> kbasePropertiesMap = new HashMap<String, String>();
    private final Map<String, String> ksessionPropertiesMap = new HashMap<String, String>();
    private final Map<String, String> kagentPropertiestMap = new HashMap<String, String>();
    private final Map<String, String> serializationSigningPropertiesMap = new HashMap<String, String>();

    @PostConstruct
    public void setup() {
        readProperties(kbuilderPropertiesMap, config.kbuilderProperties(), config.kbuilderConfigFile());
        readProperties(kbasePropertiesMap, config.kbaseProperties(), config.kbaseConfigFile());
        readProperties(ksessionPropertiesMap, config.ksessionProperties(), config.ksessionConfigFile());
        readProperties(kagentPropertiestMap, config.kagentPropertiest(), config.kagentConfigFile());
        readProperties(serializationSigningPropertiesMap, config.serializationSigningProperties(), config.serializationSigningConfigFile());
    }

    public ResourceChangeScannerConfiguration getResourceChangeScannerConfiguration() {
        ResourceChangeScannerConfiguration sconf = ResourceFactory.getResourceChangeScannerService().newResourceChangeScannerConfiguration();
        if (config.scannerInterval() >= 0) {
            sconf.setProperty("drools.resource.scanner.interval", String.valueOf(config.scannerInterval()));
        }
        return sconf;
    }

    public KnowledgeAgentConfiguration getKnowledgeAgentConfiguration() {
        KnowledgeAgentConfiguration config = KnowledgeAgentFactory.newKnowledgeAgentConfiguration();
        Iterator<Entry<String, String>> it = kagentPropertiestMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> nextExtry = it.next();
            config.setProperty(nextExtry.getKey(), nextExtry.getValue());
        }
        return config;
    }

    public KnowledgeSessionConfiguration getKnowledgeSessionConfiguration() {
        KnowledgeSessionConfiguration config = KnowledgeBaseFactory.newKnowledgeSessionConfiguration();
        Iterator<Entry<String, String>> it = ksessionPropertiesMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> nextExtry = it.next();
            config.setProperty(nextExtry.getKey(), nextExtry.getValue());
        }
        return config;
    }

    public KnowledgeBaseConfiguration getKnowledgeBaseConfiguration() {
        KnowledgeBaseConfiguration config = KnowledgeBaseFactory.newKnowledgeBaseConfiguration();
        Iterator<Entry<String, String>> it = kbasePropertiesMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> nextExtry = it.next();
            config.setProperty(nextExtry.getKey(), nextExtry.getValue());
        }
        return config;
    }

    public KnowledgeBuilderConfiguration getKnowledgeBuilderConfiguration() {
        KnowledgeBuilderConfiguration config = KnowledgeBuilderFactory.newKnowledgeBuilderConfiguration();
        Iterator<Entry<String, String>> it = kbuilderPropertiesMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> nextExtry = it.next();
            config.setProperty(nextExtry.getKey(), nextExtry.getValue());
        }
        return config;
    }

    private void readProperties(Map<String, String> propertiesMap, DroolsProperty[] properties, String propertiesPath) {
        for (DroolsProperty i : properties) {
            propertiesMap.put(i.name(), i.value());
        }
        if (propertiesPath != null && !propertiesPath.equals("")) {
            try {
                Properties kbuilderProp = ConfigUtils.loadProperties(resourceProvider, propertiesPath);
                for (Object key : kbuilderProp.keySet()) {
                    propertiesMap.put((String) key, (String) kbuilderProp.get(key));
                }
            } catch (IOException e) {
                log.error("Unable to read configuration properties file: " + propertiesPath);
            }
        } else {
            log.debug("NULL properties path specified, bypassing reading properties");
        }
    }

}
