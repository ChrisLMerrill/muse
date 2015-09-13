package org.musetest.core.events;

import org.musetest.core.*;
import org.musetest.core.step.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class PauseTestEvent extends MuseEvent
    {
    public PauseTestEvent(StepConfiguration next_step)
        {
        super(MuseEventType.Pause);
        _next_step = next_step;
        }

    public StepConfiguration getNextStep()
        {
        return _next_step;
        }

    private StepConfiguration _next_step;
    }


