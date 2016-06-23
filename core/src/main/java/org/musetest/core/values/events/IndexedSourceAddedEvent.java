package org.musetest.core.values.events;

import org.musetest.core.values.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class IndexedSourceAddedEvent extends ValueSourceChangeEvent
    {
    public IndexedSourceAddedEvent(ValueSourceConfiguration source, int index, ValueSourceConfiguration added_source)
        {
        super(source);
        _index = index;
        _added_source = added_source;
        }

    @Override
    protected void observe(ValueSourceChangeObserver observer)
        {
        observer.indexedSubsourceAdded(this, _index, _added_source);
        }

    private final int _index;
    private final ValueSourceConfiguration _added_source;
    }

