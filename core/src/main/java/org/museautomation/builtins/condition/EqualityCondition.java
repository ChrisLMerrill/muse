package org.museautomation.builtins.condition;

import org.museautomation.core.*;
import org.museautomation.core.resource.*;
import org.museautomation.core.values.*;
import org.museautomation.core.values.descriptor.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("equals")
@MuseValueSourceName("Equals (==)")
@MuseValueSourceTypeGroup("Math")
@MuseValueSourceShortDescription("Compare two sources for equality")
@MuseValueSourceLongDescription("Compare two sources, return true if equal. Object.equals() is used for the comparison.")
@MuseStringExpressionSupportImplementation(EqualityCondition.StringExpressionSupport.class)
@MuseSubsourceDescriptor(displayName = "Left", description = "Left operand", type = SubsourceDescriptor.Type.Named, name = BinaryCondition.LEFT_PARAM, defaultValue = "true")
@MuseSubsourceDescriptor(displayName = "Right", description = "Right operand", type = SubsourceDescriptor.Type.Named, name = BinaryCondition.RIGHT_PARAM, defaultValue = "false")
public class EqualityCondition extends BinaryCondition
    {
    @SuppressWarnings("unused")  // used via reflection
    public EqualityCondition(ValueSourceConfiguration config, MuseProject project) throws MuseInstantiationException
        {
        super(config, project);
        }

    @Override
    public Boolean resolveValue(MuseExecutionContext context) throws ValueSourceResolutionError
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

        context.raiseEvent(ConditionEvaluatedEventType.create(String.format("Condition (%s == %s) is %b", value1, value2, result)));
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

    @SuppressWarnings("WeakerAccess")  // needs public static access to be discovered and instantiated via reflection
    public static class StringExpressionSupport extends BinaryConditionStringExpressionSupport
        {
        @Override
        public String getOperator()
            {
            return "==";
            }

        @Override
        public String getSourceType()
            {
            return EqualityCondition.TYPE_ID;
            }
        }
    }