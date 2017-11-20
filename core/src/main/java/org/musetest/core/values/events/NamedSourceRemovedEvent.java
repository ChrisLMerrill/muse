package org.musetest.core.values.events;

import org.musetest.core.values.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class NamedSourceRemovedEvent extends NamedSourceChangedEvent
    {
    public NamedSourceRemovedEvent(ContainsNamedSources target, String name, ValueSourceConfiguration removed_source)
        {
        super(target, name, removed_source);
        _name = name;
        _source = removed_source;
        }

    @SuppressWarnings("WeakerAccess")
    public String getName()
        {
        return _name;
        }

    @SuppressWarnings("WeakerAccess")
    public ValueSourceConfiguration getRemovedSource()
        {
        return _source;
        }
    }


