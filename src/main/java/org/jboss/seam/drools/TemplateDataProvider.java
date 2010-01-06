package org.jboss.seam.drools;

import java.util.Collection;
import java.util.Map;

/**
 * Interface for template data providers.
 * 
 * @author Tihomir Surdilovic
 */
public interface TemplateDataProvider
{
   public Collection<Map<String, Object>> getTemplateData();
}
