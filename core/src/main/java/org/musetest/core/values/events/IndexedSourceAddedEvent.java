package org.musetest.core.values.events;

import org.musetest.core.values.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class IndexedSourceAddedEvent extends IndexedSourceChangeEvent
    {
    public IndexedSourceAddedEvent(ContainsIndexedSources container, int index, ValueSourceConfiguration source)
        {
        super(container, index, source);
        }
    }
