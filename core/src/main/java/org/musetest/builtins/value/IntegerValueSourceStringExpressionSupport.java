package org.musetest.builtins.value;

import org.musetest.core.*;
import org.musetest.core.values.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@SuppressWarnings("unused")  // used via reflection
public class IntegerValueSourceStringExpressionSupport extends BaseValueSourceStringExpressionSupport
    {
    @Override
    public ValueSourceConfiguration fromLiteral(String string, MuseProject project)
        {
        try
            {
            Long value = Long.parseLong(string);
            ValueSourceConfiguration config = new ValueSourceConfiguration();
            config.setType(IntegerValueSource.TYPE_ID);
            config.setValue(value);
            return config;
            }
        catch (NumberFormatException e)
            {
            return null;
            }
        }

    @Override
    public String toString(ValueSourceConfiguration config, MuseProject project)
        {
        if (config.getType().equals(IntegerValueSource.TYPE_ID))
            return config.getValue().toString();
        return null;
        }

    @Override
    public int getPriority()
        {
        return 2;
        }
    }


