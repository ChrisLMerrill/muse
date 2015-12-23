package org.musetest.builtins.value;

import org.musetest.core.*;
import org.musetest.core.values.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@SuppressWarnings("unused")  // used via reflection
public class NullValueSourceStringExpressionSupport extends BaseValueSourceStringExpressionSupport
    {
    @Override
    public ValueSourceConfiguration fromLiteral(String string, MuseProject project)
        {
        if (string.equals("null"))
            return ValueSourceConfiguration.forType("null");
        return null;
        }

    @Override
    public String toString(ValueSourceConfiguration config, MuseProject project, int depth)
        {
        if (NullValueSource.TYPE_ID.equals(config.getType()))
            return "null";
        return null;
        }
    }


