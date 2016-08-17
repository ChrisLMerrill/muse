package org.musetest.builtins.value.sysvar;

import org.musetest.core.*;
import org.musetest.core.values.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@SuppressWarnings("unused")  // used via reflection
public class SystemVariableSourceStringExpressionSupport extends BaseValueSourceStringExpressionSupport
    {
    @Override
    public ValueSourceConfiguration fromPrefixedExpression(String prefix, ValueSourceConfiguration expression, MuseProject project)
        {
        if (prefix.equals(OPERATOR))
            {
            ValueSourceConfiguration config = new ValueSourceConfiguration();
            config.setType(SystemVariableSource.TYPE_ID);
            config.setSource(expression);
            return config;
            }
        return null;
        }

    @Override
    public String toString(ValueSourceConfiguration config, MuseProject project, int depth)
        {
        if (config.getType().equals(SystemVariableSource.TYPE_ID))
            if (config.getValue() instanceof String)
                return OPERATOR + "\"" + config.getValue().toString() + "\"";
            else
                return OPERATOR + project.getValueSourceStringExpressionSupporters().toString(config.getSource(), depth + 1);
        return null;
        }

    private final static String OPERATOR = "$$";
    }

