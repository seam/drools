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
package org.jboss.seam.drools;

import java.util.Set;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;

import org.drools.runtime.Globals;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author Tihomir Surdilovic
 */
@ApplicationScoped
public class SeamDelegate implements Globals
{
   private static final Logger log = LoggerFactory.getLogger(SeamDelegate.class);
   
   @Inject
   BeanManager manager;

   private Globals delegate;

   public Object get(String name)
   {
      Set<Bean<?>> beans = manager.getBeans(name);
      
      if (beans != null && beans.size() > 0)
      {
         Bean<?> bean = beans.iterator().next();
         CreationalContext<?> context = manager.createCreationalContext(bean);
         return manager.getReference(bean, bean.getBeanClass(), context);
      }
      else
      {
         log.info("Could not find beans named: " + name);
         return delegate.get(name);
      }

   }

   public void set(String name, Object value)
   {
      delegate.set(name, value);
   }

   public void setDelegate(Globals delegate)
   {
      this.delegate = delegate;
   }

}
