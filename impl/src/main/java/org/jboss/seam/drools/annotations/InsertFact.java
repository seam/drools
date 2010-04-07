package org.jboss.seam.drools.annotations;

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
 * Insert fact into WM or EntryPoint. Also determine firing rules decisions.
 *  
 * @author Tihomir Surdilovic
 */
@InterceptorBinding
@Target({TYPE, METHOD})
@Documented
@Retention(RUNTIME)
@Inherited
public @interface InsertFact
{
   @Nonbinding int ksessionId() default -1;
   @Nonbinding boolean fireAllRules() default false;
   @Nonbinding int fireCount() default -1;
   @Nonbinding boolean fireUntilHalt() default false;
   @Nonbinding String entryPointName() default "";
}
