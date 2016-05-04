package org.musetest.core.step.events;

import org.musetest.core.step.*;
import org.musetest.core.values.events.*;

/**
 * Describes a change to a ValueSourceConfiguration.
 *
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public abstract class StepChangeEvent
    {
    public StepChangeEvent(StepConfiguration source)
        {
        _source = source;
        }

    public StepConfiguration getSource()
        {
        return _source;
        }

    protected abstract void observe(StepChangeObserver observer);

    private StepConfiguration _source;
    }


