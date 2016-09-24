package org.musetest.core.values.events;

import org.musetest.core.values.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class NamedSourceRenamedEvent extends ValueSourceChangeEvent
    {
    public NamedSourceRenamedEvent(ValueSourceConfiguration source, String new_name, String old_name, ValueSourceConfiguration renamed_source)
        {
        super(source);
        _new_name = new_name;
        _old_name = old_name;
        _source = renamed_source;
        }

    public String getNewName()
        {
        return _new_name;
        }

    public String getOldName()
        {
        return _old_name;
        }

    @Override
    public ValueSourceConfiguration getSource()
        {
        return _source;
        }

    private final String _new_name;
    private final String _old_name;
    private final ValueSourceConfiguration _source;
    }


