package org.musetest.builtins.value;

import org.musetest.core.*;
import org.musetest.core.values.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@SuppressWarnings("unused")  // used via reflection
public class NullValueSourceStringExpressionSupport implements ValueSourceStringExpressionSupport
    {
    @Override
    public ValueSourceConfiguration fromLiteral(String string, MuseProject project)
        {
        if (string.equals("null"))
            return ValueSourceConfiguration.forType("null");
        return null;
        }

    @Override
    public String toString(ValueSourceConfiguration config, MuseProject project)
        {
        if (NullValueSource.TYPE_ID.equals(config.getType()))
            return "null";
        return null;
        }

    @Override
    public int getPriority()
        {
        return 2;
        }
    }


