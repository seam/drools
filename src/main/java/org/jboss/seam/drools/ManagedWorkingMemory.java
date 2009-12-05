package org.jboss.seam.drools;

import java.io.Serializable;
import java.util.Set;

import javax.annotation.PreDestroy;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;

import org.drools.RuleBase;
import org.drools.StatefulSession;
import org.drools.rule.Rule;
import org.drools.spi.GlobalResolver;
import org.jboss.seam.el.Expressions.ValueExpression;

/**
 * A conversation-scoped Drools WorkingMemory for a named RuleBase
 * 
 * @author Gavin King
 *
 */
@ConversationScoped
//TODO implement mutable
public class ManagedWorkingMemory implements /*Mutable, */Serializable
{
   private static final long serialVersionUID = -1746942080571374743L;
   
   private String ruleBaseName;
   private StatefulSession statefulSession;
   private ValueExpression<RuleBase> ruleBase;
   
   @Inject BeanManager manager;
   
   public boolean clearDirty()
   {
      return true;
   }
   
   /**
    * The name of a Seam context variable holding an
    * instance of org.drools.RuleBase
    * 
    * @return a context variable name
    * @deprecated
    */
   public String getRuleBaseName()
   {
      return ruleBaseName;
   }
   
   /**
    * The name of a Seam context variable holding an
    * instance of org.drools.RuleBase
    * 
    * @param ruleBaseName a context variable name
    * @deprecated
    */
   public void setRuleBaseName(String ruleBaseName)
   {
      this.ruleBaseName = ruleBaseName;
   }
   
   @Produces
   public StatefulSession getStatefulSession()
   {
      if (statefulSession==null)
      {
         statefulSession = getRuleBaseFromValueBinding().newStatefulSession();
         statefulSession.setGlobalResolver( createGlobalResolver( statefulSession.getGlobalResolver() ) );
      }
      return statefulSession;
   }

   protected RuleBase getRuleBaseFromValueBinding()
   {
      RuleBase ruleBase = null;
      if (this.ruleBase!=null)
      {
         ruleBase = this.ruleBase.getValue();
      }
      else if (ruleBaseName!=null)
      {
         Set<Bean<?>> beans = manager.getBeans(ruleBaseName);
         if (beans.size() == 1) {
             //deprecated stuff
             Bean<?> bean = beans.iterator().next();
             ruleBase = (RuleBase) manager.getReference(bean, RuleBase.class, manager.createCreationalContext(bean));
         }
      }
      else
      {
         throw new IllegalStateException("No RuleBase");
      }
             
      if (ruleBase==null)
      {
         throw new IllegalStateException("RuleBase not found: " + ruleBaseName);
      }
      return ruleBase;
   }

   protected GlobalResolver createGlobalResolver(GlobalResolver delegate)
   {
      return new SeamGlobalResolver(delegate);
   }
   
   @PreDestroy
   public void destroy()
   {
      statefulSession.dispose();
   }
   
   public ValueExpression<RuleBase> getRuleBase()
   {
      return ruleBase;
   }
   
   public void setRuleBase(ValueExpression<RuleBase> ruleBase)
   {
      this.ruleBase = ruleBase;
   }
   
}
