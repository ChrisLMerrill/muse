package org.musetest.core.step.events;

import org.musetest.core.step.*;
import org.musetest.core.values.*;
import org.musetest.core.values.events.*;

/**
 * A change was made to a ValueSourceConfiguration within a StepConfiguration.
 *
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class SourceChangedEvent extends StepChangeEvent
    {
    public SourceChangedEvent(StepConfiguration step, ValueSourceChangeEvent event, String source_name)
        {
        super(step);
        _event = event;
        _name = source_name;
        }

    @Override
    protected void observe(StepChangeObserver observer)
        {
        observer.sourceChanged(this, _name, _event.getSource());
        }

    private final String _name;
    private final ValueSourceChangeEvent _event;
    }
