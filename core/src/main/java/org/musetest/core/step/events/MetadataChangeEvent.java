package org.musetest.core.step.events;

import org.musetest.core.step.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class MetadataChangeEvent extends StepChangeEvent
    {
    public MetadataChangeEvent(StepConfiguration step, String name, Object old_value, Object new_value)
        {
        super(step);
        _name = name;
        _old_value = old_value;
        _new_value = new_value;
        }

    @Override
    protected void observe(StepChangeObserver observer)
        {
        observer.metadataChanged(this, _name, _old_value, _new_value);
        }

    private final String _name;
    private final Object _old_value;
    private final Object _new_value;
    }


