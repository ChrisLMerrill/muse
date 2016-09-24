package org.musetest.core.values.events;

import org.musetest.core.values.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class IndexedSourceRemovedEvent extends ValueSourceChangeEvent
    {
    public IndexedSourceRemovedEvent(ValueSourceConfiguration source, int index, ValueSourceConfiguration removed_source)
        {
        super(source);
        _index = index;
        _removed_source = removed_source;
        }

    public int getIndex()
        {
        return _index;
        }

    public ValueSourceConfiguration getRemovedSource()
        {
        return _removed_source;
        }

    private final int _index;
    private final ValueSourceConfiguration _removed_source;
    }


