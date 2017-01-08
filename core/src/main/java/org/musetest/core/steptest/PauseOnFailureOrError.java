package org.musetest.core.steptest;

import org.musetest.core.*;
import org.musetest.core.events.*;
import org.musetest.core.execution.*;
import org.musetest.core.step.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class PauseOnFailureOrError implements MuseEventListener
    {
    public PauseOnFailureOrError(InteractiveTestRunner runner)
        {
        _runner = runner;
        }

    @Override
    public void eventRaised(MuseEvent event)
        {
        if (event.getType().equals(MuseEventType.EndStep) && ((StepEvent)event).getResult().getStatus().equals(StepExecutionStatus.ERROR)
            || event.getType().equals(MuseEventType.VerifyFailed))
            _runner.requestPause();
        }

    private final InteractiveTestRunner _runner;
    }


