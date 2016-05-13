package org.musetest.builtins.condition;

import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.events.*;
import org.musetest.core.resource.*;
import org.musetest.core.values.*;
import org.musetest.core.values.descriptor.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("equals")
@MuseValueSourceName("Equals (==)")
@MuseValueSourceTypeGroup("Math")
@MuseValueSourceShortDescription("Compare two sources for equality")
@MuseValueSourceLongDescription("Compare two sources, return true if equal. Object.equals() is used for the comparison.")
@MuseStringExpressionSupportImplementation(EqualityConditionStringExpressionSupport.class)
public class EqualityCondition extends BinaryCondition
    {
    @SuppressWarnings("unused")  // used via reflection
    public EqualityCondition(ValueSourceConfiguration config, MuseProject project) throws MuseInstantiationException
        {
        super(config, project);
        }

    @Override
    public Boolean resolveValue(StepExecutionContext context) throws ValueSourceResolutionError
        {
        Object value1 = _left.resolveValue(context);
        Object value2 = _right.resolveValue(context);

        if (value1 == null || value2 == null)  // if either is null
            return value1 == null && value2 == null;  // equal only if both are null

        boolean result;
        if (value1 instanceof Number || value2 instanceof Number)
            result = compareNumeric(value1, value2);
        else
            result = Objects.equals(value1, value2);

        context.getTestExecutionContext().raiseEvent(new ConditionEvaluatedEvent(String.format("Condition (%s == %s) is %b", value1, value2, result)));
        return result;
        }

    private boolean compareNumeric(Object value1, Object value2)
        {
        try
            {
            Number number1 = convertToNumber(value1);
            Number number2 = convertToNumber(value2);
            return number1.doubleValue() == number2.doubleValue();
            }
        catch (NumberFormatException e)
            {
            return false;
            }
        }

    private Number convertToNumber(Object value)
        {
        if (value instanceof Number)
            return (Number) value;
        else
            return Double.parseDouble(value.toString());
        }

    public final static String TYPE_ID = EqualityCondition.class.getAnnotation(MuseTypeId.class).value();
    }


