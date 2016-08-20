package org.musetest.builtins.value.property;

import org.musetest.builtins.value.*;
import org.musetest.core.*;
import org.musetest.core.values.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@SuppressWarnings("unused")  // used via reflection
public class PropertyStringExpressionSupport extends BaseValueSourceStringExpressionSupport
    {
    @Override
    public ValueSourceConfiguration fromBinaryExpression(ValueSourceConfiguration left, String operator, ValueSourceConfiguration right, MuseProject project)
        {
        if (operator.equals("."))
            {
            ValueSourceConfiguration config = ValueSourceConfiguration.forType(PropertySource.TYPE_ID);
            config.addSource(PropertySource.TARGET_PARAM, left);
            config.addSource(PropertySource.NAME_PARAM, right);
            return config;
            }
        return null;
        }

    @Override
    public String toString(ValueSourceConfiguration config, MuseProject project, int depth)
        {
        if (config.getType().equals(PropertySource.TYPE_ID))
            {
            String target = project.getValueSourceStringExpressionSupporters().toString(config.getSourceMap().get(PropertySource.TARGET_PARAM), depth + 1);
            String name = project.getValueSourceStringExpressionSupporters().toString(config.getSourceMap().get(PropertySource.NAME_PARAM), depth + 1);
            String expression = String.format("%s.%s", target, name);
            if (depth == 0)
                return expression;
            else
                return "(" + expression + ")";
            }
        return null;
        }
    }


