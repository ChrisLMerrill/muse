package org.musetest.builtins.condition;

import org.musetest.core.*;
import org.musetest.core.resource.*;
import org.musetest.core.values.*;
import org.musetest.core.values.descriptor.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("greaterthan")
@MuseValueSourceName("Greater Than (>)")
@MuseValueSourceTypeGroup("Math")
@MuseValueSourceShortDescription("True if the left is greater than the right")
@MuseValueSourceLongDescription("Compare the result of resolving two sources (left and right). Returns true if left operand is greater than the right. Integer and String comparisons are supported - other operand types will result in an error.")
@MuseStringExpressionSupportImplementation(GreaterThanCondition.StringExpressionSupport.class)
@MuseSubsourceDescriptor(displayName = "Left", description = "Left operand", type = SubsourceDescriptor.Type.Named, name = BinaryCondition.LEFT_PARAM)
@MuseSubsourceDescriptor(displayName = "Right", description = "Right operand", type = SubsourceDescriptor.Type.Named, name = BinaryCondition.RIGHT_PARAM)
public class GreaterThanCondition extends BinaryCondition
	{
	@SuppressWarnings("unused")  // used via reflection
	public GreaterThanCondition(ValueSourceConfiguration config, MuseProject project) throws MuseInstantiationException
		{
		super(config, project);
		}

	@Override
	public Boolean resolveValue(MuseExecutionContext context) throws ValueSourceResolutionError
		{
		Object left = _left.resolveValue(context);
		Object right = _right.resolveValue(context);

		boolean result;
		if (left instanceof Long && right instanceof Long)
			result = (Long) left > (Long) right;
		else if (left instanceof String && right instanceof String)
			result = ((String) left).compareTo((String) right) > 0;
		else
			throw new ValueSourceResolutionError("GreaterThanCondition can operate on 2 integers or 2 strings. Instead received: " + left + "(" + left.getClass().getSimpleName() + ") > " + right + "(" + right.getClass().getSimpleName() + ")");

		context.raiseEvent(ConditionEvaluatedEventType.create(String.format("Condition (%s>%s) is %b", left, right, result)));
		return result;
		}

	public final static String TYPE_ID = GreaterThanCondition.class.getAnnotation(MuseTypeId.class).value();

	@SuppressWarnings("WeakerAccess")  // needs public static access to be discovered and instantiated via reflection
	public static class StringExpressionSupport extends BinaryConditionStringExpressionSupport
		{
		@Override
		public String getOperator()
			{
			return ">";
			}

		@Override
		public String getSourceType()
			{
			return GreaterThanCondition.TYPE_ID;
			}
		}
	}