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
package org.jboss.seam.drools.interceptor;

import java.lang.annotation.Annotation;

import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

import org.jboss.seam.drools.annotations.InsertFact;

@InsertFact
@Interceptor
public class InsertFactInterceptor
{
   @Inject
   BeanManager manager;
   
   @AroundInvoke
   public Object insertFact(InvocationContext ctx) throws Exception
   {
      System.out.println("*******\n\nIN INTERCEPTOR! \n\n ********");
      Annotation[] methodAnnotations = ctx.getMethod().getAnnotations();
      for(Annotation nextAnnotation : methodAnnotations) {
         if(manager.isQualifier(nextAnnotation.getClass())) {
            System.out.println("**************** \n\n\nNEXT QUALIFIER: " + nextAnnotation);
         }
         if(manager.isInterceptorBinding(nextAnnotation.getClass())) {
            System.out.println("**************** \n\n\n\n NEXT INTERCEPTOR BINDING: " + nextAnnotation);   
         }
      }
      
      return ctx.proceed();
   }
}
