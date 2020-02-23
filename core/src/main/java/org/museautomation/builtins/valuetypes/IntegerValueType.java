package org.museautomation.builtins.valuetypes;

import org.museautomation.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class IntegerValueType implements MuseValueType
    {
    @Override
    public String getId()
        {
        return "integer";
        }

    @Override
    public String getName()
        {
        return "Integer";
        }

    @Override
    public boolean isInstance(Object obj)
        {
        return obj instanceof Long;
        }

    @Override
    public boolean equals(Object obj)
        {
        return obj instanceof IntegerValueType && getId().equals(((IntegerValueType) obj).getId());
        }
    }