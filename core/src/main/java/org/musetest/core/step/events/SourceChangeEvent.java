package org.musetest.core.step.events;

import org.musetest.core.step.*;
import org.musetest.core.values.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class SourceChangeEvent extends StepChangeEvent
    {
    public SourceChangeEvent(StepConfiguration source, String name, ValueSourceConfiguration old_source, ValueSourceConfiguration new_source)
        {
        super(source);
        _name = name;
        _old_source = old_source;
        _new_source = new_source;
        }

    @Override
    protected void observe(StepChangeObserver observer)
        {
        observer.sourceChanged(this, _name, _old_source, _new_source);
        }

    private final String _name;
    private final ValueSourceConfiguration _old_source;
    private final ValueSourceConfiguration _new_source;
    }


