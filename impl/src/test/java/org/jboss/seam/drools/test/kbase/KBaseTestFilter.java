package org.jboss.seam.drools.test.kbase;

import org.jboss.shrinkwrap.api.Filter;

public class KBaseTestFilter implements Filter<Class<?>>
{

   public boolean include(Class<?> clazz)
   {
      // exclude classes in all other test packages except your own
      if(clazz.getPackage().getName().startsWith("org.jboss.seam.drools.test")
            && !clazz.getPackage().getName().equals(KBaseTestFilter.class.getPackage().getName())) {
         return false;
      } else {
         return true;
      }
   }
   
}
