package org.musetest.core.values.events;

import org.musetest.core.values.*;

/**
 * Describes a change to a ValueSourceConfiguration.
 *
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public abstract class ValueSourceChangeEvent
    {
    public ValueSourceChangeEvent(ValueSourceConfiguration source)
        {
        _source = source;
        }

    public ValueSourceConfiguration getSource()
        {
        return _source;
        }

    protected abstract void observe(ValueSourceChangeObserver observer);

    private ValueSourceConfiguration _source;
    }


