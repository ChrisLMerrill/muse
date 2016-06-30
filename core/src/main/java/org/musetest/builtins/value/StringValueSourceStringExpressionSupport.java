package org.musetest.builtins.value;

import org.musetest.core.*;
import org.musetest.core.values.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@SuppressWarnings("unused")  // used via reflection
public class StringValueSourceStringExpressionSupport extends BaseValueSourceStringExpressionSupport
    {
    @Override
    public ValueSourceConfiguration fromLiteral(String string, MuseProject project)
        {
        if (string.length() > 1 && string.startsWith("\"") && string.endsWith("\""))
            {
            ValueSourceConfiguration config = new ValueSourceConfiguration();
            config.setType(StringValueSource.TYPE_ID);
            config.setValue(string.substring(1, string.length() - 1));
            return config;
            }
        return null;
        }

    @Override
    public String toString(ValueSourceConfiguration config, MuseProject project, int depth)
        {
        if (config.getType().equals(StringValueSource.TYPE_ID))
            {
            if (config.getValue() != null)
                return "\"" + config.getValue() + "\"";
            return "???";
            }
        return null;
        }
    }
