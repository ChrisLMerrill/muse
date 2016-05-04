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

    @Override
    protected void observe(ValueSourceChangeObserver observer)
        {
        observer.indexedSubsourceReplaced(this, _index, _old_source, _new_source);
        }

    private final int _index;
    private final ValueSourceConfiguration _old_source;
    private final ValueSourceConfiguration _new_source;
    }


