package org.museautomation.core.values.events;

import org.museautomation.core.values.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class NamedSourceAddedEvent extends NamedSourceChangedEvent
    {
    public NamedSourceAddedEvent(ContainsNamedSources target, String name, ValueSourceConfiguration added_source)
        {
        super(target, name, added_source);
        }

    @SuppressWarnings("WeakerAccess")
    public ValueSourceConfiguration getAddedSource()
        {
        return _source;
        }
    }


