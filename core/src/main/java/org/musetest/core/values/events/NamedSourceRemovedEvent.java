package org.musetest.core.values.events;

import org.musetest.core.values.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class NamedSourceRemovedEvent extends ValueSourceChangeEvent
    {
    public NamedSourceRemovedEvent(ValueSourceConfiguration source, String name, ValueSourceConfiguration removed_source)
        {
        super(source);
        _name = name;
        _removed_source = removed_source;
        }

    public String getName()
        {
        return _name;
        }

    public ValueSourceConfiguration getRemovedSource()
        {
        return _removed_source;
        }

    private final String _name;
    private final ValueSourceConfiguration _removed_source;
    }


