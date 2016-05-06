package org.musetest.core.step.events;

import org.musetest.core.step.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class TypeChangeEvent extends StepChangeEvent
    {
    public TypeChangeEvent(StepConfiguration step, String old_type, String new_type)
        {
        super(step);
        _old_type = old_type;
        _new_type = new_type;
        }

    @Override
    protected void observe(StepChangeObserver observer)
        {
        observer.typeChanged(this, _old_type, _new_type);
        }

    private final String _old_type;
    private final String _new_type;
    }


