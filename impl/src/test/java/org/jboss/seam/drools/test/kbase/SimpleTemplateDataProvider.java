package org.jboss.seam.drools.test.kbase;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.jboss.seam.drools.TemplateDataProvider;
import org.jboss.seam.drools.qualifiers.TemplateData;

@TemplateData(name="forkbasetest")
public class SimpleTemplateDataProvider implements TemplateDataProvider
{
   private static Collection<Map<String, Object>> templateData = new ArrayList<Map<String,Object>>();
   static {
      Map<String, Object> m1 = new HashMap<String, Object>();
      m1.put("name", "Tihomir");
      templateData.add(m1);
      Map<String, Object> m2 = new HashMap<String, Object>();
      m2.put("name", "Stuart");
      templateData.add(m2);
      Map<String, Object> m3 = new HashMap<String, Object>();
      m3.put("name", "Dan");
      templateData.add(m3);
   }
   public Collection<Map<String, Object>> getTemplateData()
   {
      return templateData;
   }
   
}
