package org.musetest.core.test;

import org.musetest.core.*;
import org.musetest.core.events.*;
import org.musetest.core.execution.*;
import org.musetest.core.step.*;
import org.musetest.core.variables.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class TestFailsOnErrorFailureOrInterrupt implements TestResultProducer
    {
    public TestFailsOnErrorFailureOrInterrupt(MuseTest test, MuseExecutionContext context)
        {
        _test = test;
        _context = context;
        }

    @Override
    public MuseTestResult getTestResult()
        {
        return new BaseMuseTestResult(_test, _context, _failure);
        }

    @Override
    public void eventRaised(MuseEvent event)
        {
        if (event instanceof StepEvent && ((StepEvent)event).getResult() != null)
            {
            StepExecutionResult step_result = ((StepEvent)event).getResult();
            if (step_result.getStatus() == StepExecutionStatus.FAILURE && _failure == null)
                _failure = new MuseTestFailureDescription(MuseTestFailureDescription.FailureType.Failure, step_result.getDescription());
            else if (step_result.getStatus() == StepExecutionStatus.ERROR)
                _failure = new MuseTestFailureDescription(MuseTestFailureDescription.FailureType.Error, step_result.getDescription());
            }
        else if (event.getTypeId().equals(InterruptedEventType.TYPE_ID))
            _failure = new MuseTestFailureDescription(MuseTestFailureDescription.FailureType.Interrupted, "interrupted by user");
        else if (event.getStatus().equals(EventStatus.Failure))
            _failure = new MuseTestFailureDescription(MuseTestFailureDescription.FailureType.Failure, event.getDescription());
        else if (event.getStatus().equals(EventStatus.Error))
            _failure = new MuseTestFailureDescription(MuseTestFailureDescription.FailureType.Error, event.getDescription());
        }

    private MuseTest _test;
	private MuseExecutionContext _context;
	private MuseTestFailureDescription _failure;
    }



