package org.jboss.seam.drools.qualifier;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.inject.Qualifier;

@Qualifier
@Target({METHOD, FIELD})
@Documented
@Retention(RUNTIME)
@Inherited
public @interface KBase
{
   String value() default "";
}
