package org.musetest.core.values.events;

import org.musetest.core.values.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class IndexedSourceReplacedEvent extends ValueSourceChangeEvent
    {
    public IndexedSourceReplacedEvent(ValueSourceConfiguration source, int index, ValueSourceConfiguration old_source, ValueSourceConfiguration new_source)
        {
        super(source);
        _index = index;
        _old_source = old_source;
        _new_source = new_source;
        }

    public int getIndex()
        {
        return _index;
        }

    public ValueSourceConfiguration getOldSource()
        {
        return _old_source;
        }

    public ValueSourceConfiguration getNewSource()
        {
        return _new_source;
        }

    private final int _index;
    private final ValueSourceConfiguration _old_source;
    private final ValueSourceConfiguration _new_source;
    }


