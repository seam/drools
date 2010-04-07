package org.jboss.seam.drools.events;

import org.drools.builder.KnowledgeBuilderErrors;

/**
 * This event is fires in case of KnowledgeBuilder errors.
 * 
 * @author Tihomir Surdilovic
 */
public class KnowledgeBuilderErrorsEvent
{
   private KnowledgeBuilderErrors errors;

   public KnowledgeBuilderErrorsEvent(KnowledgeBuilderErrors errors)
   {
      this.errors = errors;
   }

   public KnowledgeBuilderErrors getErrors()
   {
      return errors;
   }

   public void setErrors(KnowledgeBuilderErrors errors)
   {
      this.errors = errors;
   }

}
