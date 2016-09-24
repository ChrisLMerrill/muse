package org.musetest.core.values.events;

import org.musetest.core.values.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class NamedSourceAddedEvent extends ValueSourceChangeEvent
    {
    public NamedSourceAddedEvent(ValueSourceConfiguration source, String name, ValueSourceConfiguration added_source)
        {
        super(source);
        _name = name;
        _added_source = added_source;
        }

    public String getName()
        {
        return _name;
        }

    public ValueSourceConfiguration getAddedSource()
        {
        return _added_source;
        }

    private final String _name;
    private final ValueSourceConfiguration _added_source;
    }


