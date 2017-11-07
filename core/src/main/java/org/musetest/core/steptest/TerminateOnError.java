package org.musetest.core.steptest;

import org.musetest.core.*;
import org.musetest.core.events.*;
import org.musetest.core.execution.*;
import org.musetest.core.step.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class TerminateOnError implements MuseEventListener
    {
    public TerminateOnError(ThreadedTestRunner runner)
        {
        _runner = runner;
        _test_executor = null;
        _step_executor = null;
        }

    TerminateOnError(SteppedTestExecutor executor)
        {
        _test_executor = executor;
        _runner = null;
        _step_executor = null;
        }

    public TerminateOnError(StepExecutor executor)
        {
        _step_executor = executor;
        _runner = null;
        _test_executor = null;
        }


    @Override
    public void eventRaised(MuseEvent event)
        {
        if (event.getTypeId().equals(StepEvent.EndStepEventType.TYPE_ID) && ((StepEvent)event).getResult().getStatus().equals(StepExecutionStatus.ERROR)
            || event.isTerminateRequested())
            {
            if (_runner != null)
                _runner.interrupt();
            else if (_test_executor != null)
                _test_executor.requestTerminate();
            else if (_step_executor != null)
                _step_executor.requestTerminate();
            }
        }

    private final ThreadedTestRunner _runner;
    private final SteppedTestExecutor _test_executor;
    private final StepExecutor _step_executor;
    }


