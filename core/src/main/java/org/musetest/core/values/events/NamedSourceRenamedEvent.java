package org.musetest.core.values.events;

import org.musetest.core.util.*;
import org.musetest.core.values.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class NamedSourceRenamedEvent extends ChangeEvent
    {
    public NamedSourceRenamedEvent(ContainsNamedSources target, String new_name, String old_name, ValueSourceConfiguration renamed_source)
        {
        super(target);
        _new_name = new_name;
        _old_name = old_name;
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
        return _old_name;
        }

    @SuppressWarnings("WeakerAccess")
    public ValueSourceConfiguration getRenamedSource()
        {
        return _source;
        }

    private final String _new_name;
    private final String _old_name;
    private final ValueSourceConfiguration _source;
    }


