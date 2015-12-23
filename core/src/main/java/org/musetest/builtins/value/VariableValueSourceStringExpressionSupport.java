package org.musetest.builtins.value;

import org.musetest.core.*;
import org.musetest.core.values.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@SuppressWarnings("unused")  // used via reflection
public class VariableValueSourceStringExpressionSupport extends BaseValueSourceStringExpressionSupport
    {
    @Override
    public ValueSourceConfiguration fromPrefixedExpression(String prefix, ValueSourceConfiguration expression, MuseProject project)
        {
        if (prefix.equals("$"))
            {
            ValueSourceConfiguration config = new ValueSourceConfiguration();
            config.setType(VariableValueSource.TYPE_ID);
            config.setSource(expression);
            return config;
            }
        return null;
        }

    @Override
    public String toString(ValueSourceConfiguration config, MuseProject project, int depth)
        {
        if (config.getType().equals(VariableValueSource.TYPE_ID))
            return "$" + ValueSourceStringExpressionSupporters.toString(config.getSource(), project, depth+1);
        return null;
        }
    }


