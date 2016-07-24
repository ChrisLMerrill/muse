package org.musetest.core.test;

import org.musetest.builtins.step.*;
import org.musetest.core.*;
import org.musetest.core.events.*;
import org.musetest.core.step.*;
import org.musetest.core.variables.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class DefaultTestResultProducer implements TestResultProducer
    {
    public DefaultTestResultProducer(MuseTest test, EventLog log)
        {
        _test = test;
        _log = log;
        }

    @Override
    public MuseTestResult getTestResult()
        {
        return new BaseMuseTestResult(_test, _log, _failure);
        }

    @Override
    public void eventRaised(MuseEvent event)
        {
        if (event instanceof VerifyFailureEvent)
            _failure = new MuseTestFailureDescription(MuseTestFailureDescription.FailureType.Failure, event.getDescription());
        else if (event instanceof TestErrorEvent)
            {
            _failure = new MuseTestFailureDescription(MuseTestFailureDescription.FailureType.Failure, event.getDescription());
            }
        else if (event instanceof StepEvent && ((StepEvent)event).getResult() != null)
            {
            StepExecutionResult step_result = ((StepEvent)event).getResult();
            if (step_result.getStatus() == StepExecutionStatus.FAILURE && _failure == null)
                _failure = new MuseTestFailureDescription(MuseTestFailureDescription.FailureType.Failure, step_result.getDescription());
            else if (step_result.getStatus() == StepExecutionStatus.ERROR)
                _failure = new MuseTestFailureDescription(MuseTestFailureDescription.FailureType.Error, step_result.getDescription());
            }
        }

    private MuseTest _test;
    private EventLog _log;
    private MuseTestFailureDescription _failure;
    }



