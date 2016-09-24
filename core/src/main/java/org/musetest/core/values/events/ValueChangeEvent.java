package org.musetest.core.values.events;

import org.musetest.core.values.*;

/**
 * Raised when the primitive value changes.
 *
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class ValueChangeEvent extends ValueSourceChangeEvent
    {
    public ValueChangeEvent(ValueSourceConfiguration source, Object new_value, Object old_value)
        {
        super(source);
        _new_value = new_value;
        _old_value = old_value;
        }

    public Object getNewValue()
        {
        return _new_value;
        }

    public Object getOldValue()
        {
        return _old_value;
        }

    private Object _new_value;
    private Object _old_value;
    }


