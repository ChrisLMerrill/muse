package org.musetest.core.step.events;

import org.musetest.core.step.*;

/**
 * Describes a change to a ValueSourceConfiguration.
 *
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public abstract class StepChangeEvent
    {
    public StepChangeEvent(StepConfiguration step)
        {
        _step = step;
        }

    public StepConfiguration getStep()
        {
        return _step;
        }

    protected abstract void observe(StepChangeObserver observer);

    private StepConfiguration _step;
    }


