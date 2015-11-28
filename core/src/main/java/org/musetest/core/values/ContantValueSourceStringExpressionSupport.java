package org.musetest.core.values;

import org.musetest.core.*;

/**
 * Extend this to easily support a ValueSourceQuickEditSupport that maps a constant
 * string to a MuseValueSource implementation with no parameters.
 *
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public abstract class ContantValueSourceStringExpressionSupport implements ValueSourceStringExpressionSupport
    {
    public ContantValueSourceStringExpressionSupport(String constant, String type_id)
        {
        _constant = constant;
        _type_id = type_id;
        }

    public ValueSourceConfiguration parse(String string, MuseProject project)
        {
        if (string.equals(_constant))
            {
            ValueSourceConfiguration config = new ValueSourceConfiguration();
            config.setType(_type_id);
            return config;
            }
        return null;
        }

    @Override
    public String asString(ValueSourceConfiguration config, MuseProject project)
        {
        if (config.getType().equals(_type_id))
            return _constant;
        return null;
        }

    @Override
    public int getPriority()
        {
        return 2;
        }

    private final String _constant;
    private final String _type_id;
    }


