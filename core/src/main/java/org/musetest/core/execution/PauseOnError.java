package org.musetest.core.execution;

import org.musetest.core.*;
import org.musetest.core.events.*;
import org.musetest.core.step.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class PauseOnError implements MuseEventListener
    {
    public PauseOnError(InteractiveTestRunner runner)
        {
        _runner = runner;
        }

    @Override
    public void eventRaised(MuseEvent event)
        {
        if (event.getType().equals(MuseEventType.EndStep))
            {
            StepEvent step_event = (StepEvent) event;
            if (step_event.getResult().getStatus().equals(StepExecutionStatus.ERROR))
                _runner.requestPause();
            }
        }

    private final InteractiveTestRunner _runner;
    }


