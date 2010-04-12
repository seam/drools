package org.jboss.seam.drools.bootstrap;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AfterBeanDiscovery;
import javax.enterprise.inject.spi.Extension;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DroolsExtension implements Extension
{
   private static final Logger log = LoggerFactory.getLogger(DroolsExtension.class);

   void afterBeanDiscovery(@Observes AfterBeanDiscovery abd) {
      
   }

}
