package org.musetest.builtins.value;

import org.musetest.core.*;
import org.musetest.core.values.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@SuppressWarnings("unused")  // used via reflection
public class BooleanValueSourceQuickEditSupport implements ValueSourceQuickEditSupport
    {
    @Override
    public ValueSourceConfiguration parse(String string, MuseProject project)
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
    public String asString(ValueSourceConfiguration config, MuseProject project)
        {
        if (config.getType().equals(BooleanValueSource.TYPE_ID))
            return config.getValue().toString();
        return null;
        }

    @Override
    public int getPriority()
        {
        return 2;
        }
    }


