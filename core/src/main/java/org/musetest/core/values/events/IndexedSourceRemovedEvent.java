package org.musetest.core.values.events;

import org.musetest.core.values.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class IndexedSourceRemovedEvent extends IndexedSourceChangeEvent
    {
    public IndexedSourceRemovedEvent(ContainsIndexedSources source, int index, ValueSourceConfiguration removed_source)
        {
        super(source, index, removed_source);
        }
    }
