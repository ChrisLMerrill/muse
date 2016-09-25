package org.musetest.core.values.events;

import org.musetest.core.util.*;
import org.musetest.core.values.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class NamedSourceRemovedEvent extends ChangeEvent
    {
    public NamedSourceRemovedEvent(ContainsNamedSources target, String name, ValueSourceConfiguration removed_source)
        {
        super(target);
        _name = name;
        _removed_source = removed_source;
        }

    @SuppressWarnings("WeakerAccess")
    public String getName()
        {
        return _name;
        }

    @SuppressWarnings("WeakerAccess")
    public ValueSourceConfiguration getRemovedSource()
        {
        return _removed_source;
        }

    private final String _name;
    private final ValueSourceConfiguration _removed_source;
    }


