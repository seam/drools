package org.jboss.seam.drools.bootstrap.util;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class LHSTemplates
{
   //Number( intValue >= 3) from accumulate( X() from entry-point Y over window:time( 10m ), count(1) )
   public static final String MIN = "min";
   public static final String MAX = "max";
   public static final String AFTER = "after";
   public static final String BEFORE = "before";
   public static final String COINCIDES = "coincides";
   public static final String DURING = "during";
   public static final String FINISHED_BY = "finishedby";
   public static final String FINISHES = "finishes";
   public static final String INCLUDES = "includes";
   public static final String MEETS = "meets";
   public static final String MET_BY = "metby";
   public static final String OVERLAPPED_BY = "overlappedby";
   public static final String OVERLAPS = "overlaps";
   public static final String OVER_WINDOW_LENGTH = "overwindowlength";
   public static final String OVER_WINDOW_TIME = "overwindowtime";
   public static final String STARTED_BY = "startedby";
   public static final String STARTS = "starts";
   
   private Map<String, String> patterns;
   
   @PostConstruct
   public void init() {
      patterns = new HashMap<String, String>();
      
      
   }
   
   
}
