package org.musetest.builtins.condition;

import org.musetest.core.*;
import org.musetest.core.events.*;
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
@MuseStringExpressionSupportImplementation(LessThanConditionStringExpressionSupport.class)
@MuseSubsourceDescriptor(displayName = "Left", description = "Left operand", type = SubsourceDescriptor.Type.Named, name = BinaryCondition.LEFT_PARAM)
@MuseSubsourceDescriptor(displayName = "Right", description = "Right operand", type = SubsourceDescriptor.Type.Named, name = BinaryCondition.RIGHT_PARAM)
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
        Object value1 = _left.resolveValue(context);
        Object value2 = _right.resolveValue(context);

        boolean result;
        if (value1 instanceof Long && value2 instanceof Long)
            result = (Long) value1 < (Long) value2;
        else if (value1 instanceof String && value2 instanceof String)
            result = ((String) value1).compareTo((String) value2) < 0;
        else
            throw new IllegalArgumentException("LessThanCondition can operate on 2 integers or 2 strings. Instead received: " + value1 + "(" + value1.getClass().getSimpleName() + ") > " + value2 + "(" + value2.getClass().getSimpleName() + ")");

        context.raiseEvent(new ConditionEvaluatedEvent(String.format("Condition (%s<%s) is %b", value1, value2, result)));
        return result;
        }

    public final static String TYPE_ID = LessThanCondition.class.getAnnotation(MuseTypeId.class).value();
    }


