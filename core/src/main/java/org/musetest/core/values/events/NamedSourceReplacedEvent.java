package org.musetest.core.values.events;

import org.musetest.core.values.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class NamedSourceReplacedEvent extends ValueSourceChangeEvent
    {
    public NamedSourceReplacedEvent(ValueSourceConfiguration source, String name, ValueSourceConfiguration old_source, ValueSourceConfiguration new_source)
        {
        super(source);
        _name = name;
        _old_source = old_source;
        _new_source = new_source;
        }

    public String getName()
        {
        return _name;
        }

    public ValueSourceConfiguration getOldSource()
        {
        return _old_source;
        }

    public ValueSourceConfiguration getNewSource()
        {
        return _new_source;
        }

    private final String _name;
    private final ValueSourceConfiguration _old_source;
    private final ValueSourceConfiguration _new_source;
    }


