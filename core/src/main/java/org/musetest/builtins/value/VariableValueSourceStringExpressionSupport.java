package org.musetest.builtins.value;

import org.musetest.core.*;
import org.musetest.core.values.*;

import java.util.*;

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
    public String toString(ValueSourceConfiguration config, MuseProject project)
        {
        if (config.getType().equals(VariableValueSource.TYPE_ID))
            {
            if (config.getValue() != null)
                return "$\"" + config.getValue().toString() + "\"";
            else
                return ValueSourceStringExpressionSupporters.toString(config.getSource(), project);
            }
        return null;
        }

    @Override
    public int getPriority()
        {
        return 2;
        }
    }

