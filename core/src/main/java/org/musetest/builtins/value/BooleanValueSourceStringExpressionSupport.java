package org.musetest.builtins.value;

import org.musetest.core.*;
import org.musetest.core.values.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@SuppressWarnings("unused")  // used via reflection
public class BooleanValueSourceStringExpressionSupport extends BaseValueSourceStringExpressionSupport
    {
    @Override
    public ValueSourceConfiguration fromLiteral(String string, MuseProject project)
        {
        Boolean value = null;
        if (string.toLowerCase().equals("true"))
            value = true;
        else if (string.toLowerCase().equals("false"))
            value = false;

        if (value == null)
            return null;

        ValueSourceConfiguration config = new ValueSourceConfiguration();
        config.setType(BooleanValueSource.TYPE_ID);
        config.setValue(value);
        return config;
        }

    @Override
    public String toString(ValueSourceConfiguration config, MuseProject project, int depth)
        {
        if (config.getType().equals(BooleanValueSource.TYPE_ID))
            {
            if (config.getValue() != null)
                return config.getValue().toString();
            return "???";
            }
        return null;
        }
    }


