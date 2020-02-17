package org.museautomation.core.steptask;

import org.museautomation.core.*;
import org.museautomation.core.events.*;
import org.museautomation.core.execution.*;
import org.museautomation.core.step.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class TerminateOnError implements MuseEventListener
    {
    public TerminateOnError(ThreadedTaskRunner runner)
        {
        _runner = runner;
        _test_executor = null;
        _step_executor = null;
        }

    TerminateOnError(SteppedTaskExecutor executor)
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
        if (event.getTypeId().equals(EndStepEventType.TYPE_ID) && event.hasTag(MuseEvent.ERROR)
            || event.hasTag(MuseEvent.TERMINATE))
            {
            if (_runner != null)
                _runner.interrupt();
            else if (_test_executor != null)
                _test_executor.requestTerminate();
            else if (_step_executor != null)
                _step_executor.requestTerminate();
            }
        }

    private final ThreadedTaskRunner _runner;
    private final SteppedTaskExecutor _test_executor;
    private final StepExecutor _step_executor;
    }