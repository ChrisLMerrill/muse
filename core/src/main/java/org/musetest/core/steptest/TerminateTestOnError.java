package org.musetest.core.steptest;

import org.musetest.core.*;
import org.musetest.core.events.*;
import org.musetest.core.step.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class TerminateTestOnError implements MuseEventListener
    {
    public TerminateTestOnError(SteppedTestExecutor executor)
        {
        _executor = executor;
        }

    @Override
    public void eventRaised(MuseEvent event)
        {
        if (event.getType().equals(MuseEventType.EndStep) && ((StepEvent)event).getResult().getStatus().equals(StepExecutionStatus.ERROR))
            _executor.requestTerminate();
        }

    private final SteppedTestExecutor _executor;
    }


