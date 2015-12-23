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

    public abstract String getOperator();
    public abstract String getSourceType();

    @Override
    public String toString(ValueSourceConfiguration config, MuseProject project, int depth)
        {
        if (config.getType().equals(getSourceType()))
            {
            String left = project.getValueSourceStringExpressionSupporters().toString(config.getSourceMap().get(BinaryCondition.LEFT_PARAM), depth + 1);
            String right = project.getValueSourceStringExpressionSupporters().toString(config.getSourceMap().get(BinaryCondition.RIGHT_PARAM), depth + 1);
            String expression = String.format("%s %s %s", left, getOperator(), right);
            if (depth == 0)
                return expression;
            else
                return "(" + expression + ")";
            }
        return null;
        }
    }


