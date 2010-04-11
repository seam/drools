package org.jboss.seam.drools.interceptor;

import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

import org.jboss.seam.drools.annotations.InsertFact;

@InsertFact
@Interceptor
public class InsertInterceptor
{
   @AroundInvoke
   public Object manageTransaction(InvocationContext ctx) throws Exception
   {
      InsertFact insertFactAnnotation = ctx.getMethod().getAnnotation(InsertFact.class);
      System.out.println("ksession id: " + insertFactAnnotation.ksessionId());

      return ctx.proceed();
   }
}
