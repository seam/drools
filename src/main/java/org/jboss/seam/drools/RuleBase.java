package org.jboss.seam.drools;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.enterprise.inject.Produces;

import org.drools.RuleBaseFactory;
import org.drools.compiler.DroolsError;
import org.drools.compiler.PackageBuilder;
import org.drools.compiler.PackageBuilderConfiguration;
import org.drools.compiler.RuleError;
import org.jboss.seam.resources.ResourceLoader;
import org.jboss.webbeans.log.Log;
import org.jboss.webbeans.log.Logger;

/**
 * Manager component for a Drools RuleBase
 * 
 * @author Gavin King
 *
 */
@ApplicationScoped
public class RuleBase
{
   @Logger Log log;
   
   @Inject ResourceLoader resourceLoader;

   private String[] ruleFiles;
   private String dslFile;
   private org.drools.RuleBase ruleBase;
   
   @Inject
   public void compileRuleBase() throws Exception
   {
      PackageBuilderConfiguration conf = new PackageBuilderConfiguration();
      PackageBuilder builder = new PackageBuilder(conf);
      
      if (ruleFiles!=null)
      {
         for (String ruleFile: ruleFiles)
         {
            log.info("parsing rules: " + ruleFile);
            InputStream stream = getResourceAsStream(ruleFile);
            if (stream==null)
            {
               throw new IllegalStateException("could not locate rule file: " + ruleFile);
            }
            // read in the source
            Reader drlReader = new InputStreamReader(stream);
                                  
            if (dslFile==null)
            {
               builder.addPackageFromDrl(drlReader);
            }
            else
            {
               Reader dslReader = new InputStreamReader( getResourceAsStream(dslFile) );
               builder.addPackageFromDrl(drlReader, dslReader);
            }
            
            if ( builder.hasErrors() )
            {
               log.error("errors parsing rules in: " + ruleFile);
               for ( DroolsError error: builder.getErrors().getErrors() )
               {
                  if (error instanceof RuleError)
                  {
                     RuleError ruleError = (RuleError) error;
                     log.error( ruleError.getMessage() + " (" + ruleFile + ':' + ruleError.getLine() + ')' );
                  }
                  else
                  {
                     log.error( error.getMessage() + " (" + ruleFile + ')' );
                  }
               }
            }
         }
      }
      
      // add the package to a rulebase
      ruleBase = RuleBaseFactory.newRuleBase();
      ruleBase.addPackage( builder.getPackage() );
   }
   
   @Produces
   public org.drools.RuleBase getRuleBase()
   {
      return ruleBase;
   }
   
   public String[] getRuleFiles()
   {
      return ruleFiles;
   }
   
   public void setRuleFiles(String[] ruleFiles)
   {
      this.ruleFiles = ruleFiles;
   }
   
   public String getDslFile()
   {
      return dslFile;
   }
   
   public void setDslFile(String dslFile)
   {
      this.dslFile = dslFile;
   }
   
   public InputStream getResourceAsStream(String resource)
   {
      return resourceLoader.getResourceAsStream(resource);
   }
}
