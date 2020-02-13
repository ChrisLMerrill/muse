package org.museautomation.core.step.events;

import org.museautomation.core.step.*;
import org.museautomation.core.values.events.*;

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

    public String getName()
        {
        return _name;
        }

    public ValueSourceChangeEvent getEvent()
        {
        return _event;
        }

    private final String _name;
    private final ValueSourceChangeEvent _event;
    }
