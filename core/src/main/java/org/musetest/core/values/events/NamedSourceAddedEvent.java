package org.musetest.core.values.events;

import org.musetest.core.util.*;
import org.musetest.core.values.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class NamedSourceAddedEvent extends ChangeEvent
    {
    public NamedSourceAddedEvent(ContainsNamedSources target, String name, ValueSourceConfiguration added_source)
        {
        super(target);
        _name = name;
        _added_source = added_source;
        }

    @SuppressWarnings("WeakerAccess")
    public String getName()
        {
        return _name;
        }

    @SuppressWarnings("WeakerAccess")
    public ValueSourceConfiguration getAddedSource()
        {
        return _added_source;
        }

    private final String _name;
    private final ValueSourceConfiguration _added_source;
    }


