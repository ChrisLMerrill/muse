package org.musetest.core.values.events;

import org.musetest.core.values.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class NamedSourceReplacedEvent extends NamedSourceChangedEvent
    {
    public NamedSourceReplacedEvent(ContainsNamedSources target, String name, ValueSourceConfiguration old_source, ValueSourceConfiguration new_source)
        {
        super(target, name, old_source);
        _new_source = new_source;
        }

    @SuppressWarnings("WeakerAccess")
    public ValueSourceConfiguration getOldSource()
        {
        return _source;
        }

    @SuppressWarnings("WeakerAccess")
    public ValueSourceConfiguration getNewSource()
        {
        return _new_source;
        }

    private final ValueSourceConfiguration _new_source;
    }


