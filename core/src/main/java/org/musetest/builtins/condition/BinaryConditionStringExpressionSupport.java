package org.musetest.builtins.condition;

import org.musetest.core.*;
import org.musetest.core.values.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@SuppressWarnings("unused")  // used via reflection
public abstract class BinaryConditionStringExpressionSupport extends BaseValueSourceStringExpressionSupport
    {
    @Override
    public ValueSourceConfiguration fromBinaryExpression(ValueSourceConfiguration left, String operator, ValueSourceConfiguration right, MuseProject project)
        {
        if (operator.equals(getOperator()))
            {
            ValueSourceConfiguration config = new ValueSourceConfiguration();
            config.setType(getSourceType());
            config.addSource(BinaryCondition.LEFT_PARAM, left);
            config.addSource(BinaryCondition.RIGHT_PARAM, right);
            return config;
            }
        return null;
        }

    @Override
    public int getPriority()
        {
        return 1;
        }

    public abstract String getOperator();
    public abstract String getSourceType();

    @Override
    public String toString(ValueSourceConfiguration config, MuseProject project)
        {
        if (config.getType().equals(getSourceType()))
            {
            String left = ValueSourceStringExpressionSupporters.toString(config.getSourceMap().get(BinaryCondition.LEFT_PARAM), project);
            String right = ValueSourceStringExpressionSupporters.toString(config.getSourceMap().get(BinaryCondition.RIGHT_PARAM), project);
            return String.format("%s %s %s", left, getOperator(), right);
            }
        return null;
        }
    }


