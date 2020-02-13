package org.museautomation.core.step.events;

import org.museautomation.core.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class MetadataChangeEvent extends ChangeEvent
    {
    public MetadataChangeEvent(ContainsMetadata container, String name, Object old_value, Object new_value)
        {
        super(container);
        _name = name;
        _old_value = old_value;
        _new_value = new_value;
        }

    public String getName()
        {
        return _name;
        }

    @SuppressWarnings("WeakerAccess")
    public Object getOldValue()
        {
        return _old_value;
        }

    @SuppressWarnings("WeakerAccess")
    public Object getNewValue()
        {
        return _new_value;
        }

    private final String _name;
    private final Object _old_value;
    private final Object _new_value;
    }


