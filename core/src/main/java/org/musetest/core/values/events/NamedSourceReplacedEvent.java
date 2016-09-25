package org.musetest.core.values.events;

import org.musetest.core.util.*;
import org.musetest.core.values.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class NamedSourceReplacedEvent extends ChangeEvent
    {
    public NamedSourceReplacedEvent(ContainsNamedSources target, String name, ValueSourceConfiguration old_source, ValueSourceConfiguration new_source)
        {
        super(target);
        _name = name;
        _old_source = old_source;
        _new_source = new_source;
        }

    public String getName()
        {
        return _name;
        }

    @SuppressWarnings("WeakerAccess")
    public ValueSourceConfiguration getOldSource()
        {
        return _old_source;
        }

    @SuppressWarnings("WeakerAccess")
    public ValueSourceConfiguration getNewSource()
        {
        return _new_source;
        }

    private final String _name;
    private final ValueSourceConfiguration _old_source;
    private final ValueSourceConfiguration _new_source;
    }


