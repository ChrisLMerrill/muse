package org.museautomation.builtins.valuetypes;

import org.museautomation.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class AnyValueType implements MuseValueType
    {
    @Override
    public String getId()
        {
        return "any";
        }

    @Override
    public String getName()
        {
        return "Any";
        }

    @Override
    public boolean isInstance(Object obj)
        {
        return true;
        }

    @Override
    public boolean equals(Object obj)
        {
        return obj instanceof AnyValueType && getId().equals(((AnyValueType) obj).getId());
        }
    }