package org.jboss.seam.drools;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.Collection;
import java.util.Map;

/**
 * Interface for template data providers.
 *
 * @author Tihomir Surdilovic
 */
public interface TemplateDataProvider
{
   public Collection<Map<String,Object>> getTemplateData();
}

