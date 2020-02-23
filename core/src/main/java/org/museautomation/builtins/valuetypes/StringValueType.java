package org.museautomation.builtins.valuetypes;

import org.museautomation.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class StringValueType implements MuseValueType
    {
    @Override
    public String getId()
        {
        return "string";
        }

    @Override
    public String getName()
        {
        return "String";
        }

    @Override
    public boolean isInstance(Object obj)
        {
        return obj instanceof String;
        }

    @Override
    public boolean equals(Object obj)
        {
        return obj instanceof StringValueType && getId().equals(((StringValueType) obj).getId());
        }
    }