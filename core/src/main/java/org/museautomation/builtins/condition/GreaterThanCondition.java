package org.museautomation.builtins.condition;

import org.museautomation.core.*;
import org.museautomation.core.resource.*;
import org.museautomation.core.values.*;
import org.museautomation.core.values.descriptor.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("greaterthan")
@MuseValueSourceName("Greater Than (>)")
@MuseValueSourceTypeGroup("Math")
@MuseValueSourceShortDescription("True if the left is greater than the right")
@MuseValueSourceLongDescription("Compare the result of resolving two sources (left and right). Returns true if left operand is greater than the right. Integer and String comparisons are supported - other operand types will result in an error.")
@MuseStringExpressionSupportImplementation(GreaterThanCondition.StringExpressionSupport.class)
@MuseSubsourceDescriptor(displayName = "Left", description = "Left operand", type = SubsourceDescriptor.Type.Named, name = BinaryCondition.LEFT_PARAM, defaultValue = "99")
@MuseSubsourceDescriptor(displayName = "Right", description = "Right operand", type = SubsourceDescriptor.Type.Named, name = BinaryCondition.RIGHT_PARAM, defaultValue = "1")
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
        Object left = getValue(_left, context, false, Object.class);
        Object right = getValue(_right, context, false, Object.class);

		boolean result;
		if (left instanceof Number && right instanceof Number)
			result = ((Number) left).longValue() > ((Number) right).longValue();
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