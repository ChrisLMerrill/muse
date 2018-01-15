package org.musetest.core.values.events;

import org.musetest.core.values.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class IndexedSourceReplacedEvent extends IndexedSourceChangeEvent
    {
    public IndexedSourceReplacedEvent(ContainsIndexedSources source, int index, ValueSourceConfiguration old_source, ValueSourceConfiguration new_source)
        {
        super(source, index, old_source);
        _new_source = new_source;
        }

    public ValueSourceConfiguration getNewSource()
        {
        return _new_source;
        }

    private final ValueSourceConfiguration _new_source;
    }
