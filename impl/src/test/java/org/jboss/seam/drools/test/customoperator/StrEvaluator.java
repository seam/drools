/*
 * JBoss, Home of Professional Open Source
 * Copyright ${year}, Red Hat, Inc., and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.seam.drools.test.customoperator;

import org.drools.base.BaseEvaluator;
import org.drools.base.ValueType;
import org.drools.base.evaluators.Operator;
import org.drools.common.InternalWorkingMemory;
import org.drools.rule.VariableRestriction.ObjectVariableContextEntry;
import org.drools.rule.VariableRestriction.VariableContextEntry;
import org.drools.spi.FieldValue;
import org.drools.spi.InternalReadAccessor;
import org.jboss.seam.drools.test.customoperator.StrEvaluatorDefinition.Operations;

/**
 * 
 * @author Tihomir Surdilovic
 */
public class StrEvaluator extends BaseEvaluator {

	public static final Operator STR_COMPARE = Operator.addOperatorToRegistry(
			"str", false);
	public static final Operator NOT_STR_COMPARE = Operator
			.addOperatorToRegistry("str", true);
	public static final String[] SUPPORTED_IDS = { STR_COMPARE
			.getOperatorString() };
	
	private Operations parameter;

	public void setParameterText(String parameterText) {
		this.parameter = Operations.valueOf(parameterText);
	}

	public Operations getParameter() {
		return parameter;
	}

	public StrEvaluator(final ValueType type, final boolean isNegated) {
		super(type, isNegated ? NOT_STR_COMPARE : STR_COMPARE);
	}

	public boolean evaluate(InternalWorkingMemory workingMemory,
			InternalReadAccessor extractor, Object object, FieldValue value) {
		final Object objectValue = extractor
.getValue(workingMemory, object);
			switch (parameter) {
			case startsWith:
				return this.getOperator().isNegated() ^ (((String)objectValue).startsWith( (String)value.getValue() ));
			case endsWith:
				return this.getOperator().isNegated() ^ (((String)objectValue).endsWith( (String)value.getValue() ));
			case length:
				return this.getOperator().isNegated() ^ (((String)objectValue).length() == ((Long) value.getValue()).longValue() );
			default:
				throw new IllegalAccessError("Illegal str comparison parameter");
			}
	}

	public boolean evaluate(InternalWorkingMemory workingMemory,
			InternalReadAccessor leftExtractor, Object left,
			InternalReadAccessor rightExtractor, Object right) {
		final Object value1 = leftExtractor.getValue(workingMemory, left);
		final Object value2 = rightExtractor.getValue(workingMemory, right);
		
			switch (parameter) {
			case startsWith:
				return this.getOperator().isNegated() ^ (((String)value1).startsWith( (String) value2 ));
			case endsWith:
				return this.getOperator().isNegated() ^ (((String)value1).endsWith( (String) value2 ));
			case length:
				return this.getOperator().isNegated() ^ (((String)value1).length() == ((Long) value2).longValue() );
			default:
				throw new IllegalAccessError("Illegal str comparison parameter");
			}				
		
	}

	public boolean evaluateCachedLeft(InternalWorkingMemory workingMemory,
			VariableContextEntry context, Object right) {
		
			switch (parameter) {
			case startsWith:
				return this.getOperator().isNegated() ^ (((String)right).startsWith( (String)((ObjectVariableContextEntry)
						context).left) );
			case endsWith:
				return this.getOperator().isNegated() ^ (((String)right).endsWith( (String)((ObjectVariableContextEntry)
						context).left));
			case length:
				return this.getOperator().isNegated() ^ (((String)right).length() ==  ((Long)((ObjectVariableContextEntry)
						context).left).longValue());
			default:
				throw new IllegalAccessError("Illegal str comparison parameter");
			}
		
	}

	public boolean evaluateCachedRight(InternalWorkingMemory workingMemory,
			VariableContextEntry context, Object left) {
			switch (parameter) {
			case startsWith:
				return this.getOperator().isNegated() ^ (((String)left).startsWith((String)((ObjectVariableContextEntry)
						context).right));
			case endsWith:
				return this.getOperator().isNegated() ^ (((String)left).endsWith((String)((ObjectVariableContextEntry)
						context).right));
			case length:
				return this.getOperator().isNegated() ^ (((String)left).length() == ((Long)((ObjectVariableContextEntry)
						context).right).longValue());
			default:
				throw new IllegalAccessError("Illegal str comparison parameter");
			}
		
	}

	@Override
	public String toString() {
		return "StrEvaluatorDefinition str";

	}

}
