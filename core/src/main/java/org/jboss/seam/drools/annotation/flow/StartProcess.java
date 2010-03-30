package org.jboss.seam.drools.annotation.flow;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.enterprise.util.Nonbinding;
import javax.interceptor.InterceptorBinding;

/**
 * Starts a Rule Flow process.
 *  
 * @author Tihomir Surdilovic
 */
@InterceptorBinding
@Target({TYPE, METHOD})
@Documented
@Retention(RUNTIME)
public @interface StartProcess
{
   @Nonbinding String processId() default "";
   @Nonbinding String sessionId() default "";
}
