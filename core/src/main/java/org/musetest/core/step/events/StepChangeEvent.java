package org.musetest.core.step.events;

import org.musetest.core.step.*;
import org.musetest.core.util.*;

/**
 * Describes a change to a ValueSourceConfiguration.
 *
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
abstract class StepChangeEvent extends ChangeEvent
    {
    StepChangeEvent(StepConfiguration step)
        {
        super(step);
        }

    public StepConfiguration getStep()
        {
        return (StepConfiguration) _target;
        }

    }


