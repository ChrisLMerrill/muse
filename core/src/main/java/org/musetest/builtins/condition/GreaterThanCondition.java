package org.musetest.builtins.condition;

import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.events.*;
import org.musetest.core.resource.*;
import org.musetest.core.steptest.*;
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
@MuseStringExpressionSupportImplementation(GreaterThanConditionStringExpressionSupport.class)
public class GreaterThanCondition extends BinaryCondition
    {
    @SuppressWarnings("unused")  // used via reflection
    public GreaterThanCondition(ValueSourceConfiguration config, MuseProject project) throws MuseInstantiationException
        {
        super(config, project);
        }

    @Override
    public Boolean resolveValue(StepExecutionContext context) throws StepConfigurationError
        {
        Object left = _left.resolveValue(context);
        Object right = _right.resolveValue(context);

        boolean result;
        if (left instanceof Long && right instanceof Long)
            result = (Long) left > (Long) right;
        else if (left instanceof String && right instanceof String)
            result = ((String) left).compareTo((String) right) > 0;
        else
            throw new IllegalArgumentException("GreaterThanCondition can operate on 2 integers or 2 strings. Instead received: " + left + "(" + left.getClass().getSimpleName() + ") > " + right + "(" + right.getClass().getSimpleName() + ")");

        context.getTestExecutionContext().raiseEvent(new ConditionEvaluatedEvent(String.format("Condition (%s>%s) is %b", left, right, result)));
        return result;
        }

    public final static String TYPE_ID = GreaterThanCondition.class.getAnnotation(MuseTypeId.class).value();
    }


