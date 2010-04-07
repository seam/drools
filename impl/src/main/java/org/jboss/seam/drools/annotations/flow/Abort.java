package org.jboss.seam.drools.annotations.flow;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.enterprise.util.Nonbinding;
import javax.interceptor.InterceptorBinding;

/**
 * Abort the process instace.
 *  
 * @author Tihomir Surdilovic
 */
@InterceptorBinding
@Target({TYPE, METHOD})
@Documented
@Retention(RUNTIME)
@Inherited
public @interface Abort
{
   @Nonbinding long processid();
}
