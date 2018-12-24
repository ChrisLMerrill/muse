package org.musetest.builtins.condition;

import org.musetest.core.*;
import org.musetest.core.resource.*;
import org.musetest.core.values.*;
import org.musetest.core.values.descriptor.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("lessthan")
@MuseValueSourceName("Less Than (<)")
@MuseValueSourceTypeGroup("Math")
@MuseValueSourceShortDescription("True if the left is less than the right")
@MuseValueSourceLongDescription("Compare the result of comparing two sources (left and right). Returns true if left operand is less than the right. Integer and String comparisons are supported - other operand types will result in an error.")
@MuseStringExpressionSupportImplementation(LessThanCondition.StringExpressionSupport.class)
@MuseSubsourceDescriptor(displayName = "Left", description = "Left operand", type = SubsourceDescriptor.Type.Named, name = BinaryCondition.LEFT_PARAM, defaultValue = "1")
@MuseSubsourceDescriptor(displayName = "Right", description = "Right operand", type = SubsourceDescriptor.Type.Named, name = BinaryCondition.RIGHT_PARAM, defaultValue = "99")
public class LessThanCondition extends BinaryCondition
    {
    @SuppressWarnings("unused")  // used via reflection
    public LessThanCondition(ValueSourceConfiguration config, MuseProject project) throws MuseInstantiationException
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
            result = ((Number) left).longValue() < ((Number) right).longValue();
        else if (left instanceof String && right instanceof String)
            result = ((String) left).compareTo((String) right) < 0;
        else
            throw new ValueSourceResolutionError("LessThanCondition can operate on 2 integers or 2 strings. Instead received: " + left + "(" + left.getClass().getSimpleName() + ") < " + right + "(" + right.getClass().getSimpleName() + ")");

        context.raiseEvent(ConditionEvaluatedEventType.create(String.format("Condition (%s<%s) is %b", left, right, result)));
        return result;
        }

    public final static String TYPE_ID = LessThanCondition.class.getAnnotation(MuseTypeId.class).value();

    @SuppressWarnings("WeakerAccess")  // needs public static access to be discovered and instantiated via reflection
    public static class StringExpressionSupport extends BinaryConditionStringExpressionSupport
        {
        @Override
        public String getOperator()
            {
            return "<";
            }

        @Override
        public String getSourceType()
            {
            return LessThanCondition.TYPE_ID;
            }
        }
    }