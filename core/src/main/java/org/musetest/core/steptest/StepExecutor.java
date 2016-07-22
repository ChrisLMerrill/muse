package org.musetest.core.steptest;

import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.events.*;
import org.musetest.core.step.*;
import org.musetest.core.test.*;
import org.musetest.core.variables.*;
import org.slf4j.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class StepExecutor
    {
/*
    public StepExecutor(ContainsStep step_holder, StepExecutionContext context)
        {
        _context = context;
        _step_holder = step_holder;
        _context.addEventListener(_log);
        }

    public MuseTestResult executeAll()
        {
        if (!startTest())
            finishTest();

        boolean had_a_step_to_run = true;
        while (had_a_step_to_run && !_terminate)
            had_a_step_to_run = executeNextStep();

        return finishTest();
        }

    public boolean startTest()
        {
        _context.raiseEvent(new StartTestEvent(_test));
        _terminate = false;
        _failure = null;

        if (!_test.initializeContext(_context))
            {
            setFailure(new MuseTestFailureDescription(MuseTestFailureDescription.FailureType.Error, "Unable to initialize the context"));
            return false;
            }

        _context.getExecutionStack().push(new SingleStepExecutionContext(_context, _test.getStep(), true));

        return true;
        }

    public MuseTestResult finishTest()
        {
        MuseTestResult result = new BaseMuseTestResult(_test, _log, _failure);
        _context.raiseEvent(new EndTestEvent(result));
        _context.cleanup();
        return result;
        }

    public void setFailure(MuseTestFailureDescription failure)
        {
        _failure = failure;
        if (!failure.getFailureType().equals(MuseTestFailureDescription.FailureType.Failure))
            _terminate = true;
        }

    */
/**
     * @return false if there are no more steps to run.
     *//*

    public boolean executeNextStep()
        {
        if (!_context.getExecutionStack().hasMoreSteps())
            return false;

        StepExecutionContext step_context = _context.getExecutionStack().peek();
        String error_message = null;
        StepExecutionResult step_result = null;
        MuseStep step = null;
        try
            {
            step = step_context.getCurrentStep();
            step_result = step.execute(step_context);

            if (step_result.getStatus() == StepExecutionStatus.FAILURE && _failure == null)
                _failure = new MuseTestFailureDescription(MuseTestFailureDescription.FailureType.Failure, step_result.getDescription());
            else if (step_result.getStatus() == StepExecutionStatus.ERROR)
                _failure = new MuseTestFailureDescription(MuseTestFailureDescription.FailureType.Error, step_result.getDescription());
            }
        catch (StepConfigurationError error)
            {
            error_message = "step error due to configuration problem: " + error.getMessage();
            }
        catch (StepExecutionError error)
            {
            error_message = "step failed to execute: " + error.getMessage();
            }
        catch (Throwable t)
            {
            LOG.error("Unexpected error caught while executing step", t);
            error_message = "cannot execute the step due to an unexpected error: " + t.getMessage();
            }

        if (error_message != null)
            {
            step_result = new BasicStepExecutionResult(StepExecutionStatus.ERROR, error_message);
            if (step != null)
                _context.raiseEvent(new StepEvent(MuseEventType.EndStep, step.getConfiguration(), step_context, step_result));
            setFailure(new MuseTestFailureDescription(MuseTestFailureDescription.FailureType.Error, error_message));
            }
        if (step != null && !step_result.getStatus().equals(StepExecutionStatus.INCOMPLETE))
            step_context.stepComplete(step, step_result);

        return true;
        }

    public StepConfiguration getNextStep()
        {
        if (_context.getExecutionStack().hasMoreSteps())
            return _context.getExecutionStack().peek().getCurrentStepConfiguration();
        return null;
        }

    private ContainsStep _step_holder;
    private StepExecutionContext _context;
    @Deprecated
    private SteppedTest _test;
    private final EventLog _log = new EventLog();
    private boolean _terminate = false;
    private MuseTestFailureDescription _failure;

    private final static Logger LOG = LoggerFactory.getLogger(StepExecutor.class);
*/
    }


