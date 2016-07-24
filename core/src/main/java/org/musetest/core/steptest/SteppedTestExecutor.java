package org.musetest.core.steptest;

import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.events.*;
import org.musetest.core.step.*;
import org.musetest.core.test.*;
import org.slf4j.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class SteppedTestExecutor
    {
    public SteppedTestExecutor(SteppedTest test, SteppedTestExecutionContext context)
        {
        _context = context;
        _test = test;
        EventLog log = new EventLog();
        _resulter = new DefaultTestResultProducer(test, log);

        _context.addEventListener(_resulter);
        _context.addEventListener(log);
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

        if (!_test.initializeContext(_context))
            {
            _context.raiseEvent(new TestErrorEvent("Unable to initialize the context"));
            return false;
            }

        _context.getExecutionStack().push(new SingleStepExecutionContext(_context, _test.getStep(), true));

        return true;
        }

    public MuseTestResult finishTest()
        {
//        MuseTestResult result = new BaseMuseTestResult(_test, _log, _failure);
        MuseTestResult result = _resulter.getTestResult();
        _context.raiseEvent(new EndTestEvent(result));
        _context.cleanup();
        return result;
        }

    /**
     * @return false if there are no more steps to run.
     */
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

        StepConfiguration step_config;
        if (step != null)
            step_config = step.getConfiguration();
        else
            step_config = step_context.getCurrentStepConfiguration();

        if (error_message != null) // if the step did not complete normally, the EndStep event needs to be generated here, since it likely was not generated by the step itself.
            {
            step_result = new BasicStepExecutionResult(StepExecutionStatus.ERROR, error_message);
            if (step_config != null)
                {
                _context.raiseEvent(new StepEvent(MuseEventType.EndStep, step_config, step_context, step_result));
// TODO remove
                _terminate = true;
                }
// TODO remove
//setFailure(new MuseTestFailureDescription(MuseTestFailureDescription.FailureType.Error, error_message));
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

    private SteppedTestExecutionContext _context;
    private SteppedTest _test;
    private boolean _terminate = false;
    private TestResultProducer _resulter;

    private final static Logger LOG = LoggerFactory.getLogger(SteppedTestExecutor.class);
    }


