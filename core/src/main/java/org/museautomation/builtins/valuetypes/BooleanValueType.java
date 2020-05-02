package org.museautomation.builtins.valuetypes;

import org.museautomation.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class BooleanValueType implements MuseValueType
    {
    @Override
    public String getId()
        {
        return "boolean";
        }

    @Override
    public String getName()
        {
        return "Boolean";
        }

    @Override
    public boolean isInstance(Object obj)
        {
        return obj instanceof Boolean;
        }

    @Override
    public boolean equals(Object obj)
        {
        return obj instanceof BooleanValueType && getId().equals(((BooleanValueType) obj).getId());
        }
    }