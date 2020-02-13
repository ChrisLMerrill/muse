package org.museautomation.core.values.events;

import org.museautomation.core.values.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class NamedSourceRenamedEvent extends NamedSourceChangedEvent
    {
    public NamedSourceRenamedEvent(ContainsNamedSources target, String new_name, String old_name, ValueSourceConfiguration renamed_source)
        {
        super(target, old_name, renamed_source);
        _new_name = new_name;
        _name = old_name;
        _source = renamed_source;
        }

    @SuppressWarnings("WeakerAccess")
    public String getNewName()
        {
        return _new_name;
        }

    @SuppressWarnings("WeakerAccess")
    public String getOldName()
        {
        return _name;
        }

    @SuppressWarnings("WeakerAccess")
    public ValueSourceConfiguration getRenamedSource()
        {
        return _source;
        }

    private final String _new_name;
    }


