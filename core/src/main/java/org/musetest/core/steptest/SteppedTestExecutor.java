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
        _context.addEventListener(_log);
        }

    public MuseTestResult executeAll()
        {
        if (!startTest())
            finishTest();

        boolean had_a_step_to_run = true;
        while (had_a_step_to_run && _test_status != MuseTestResultStatus.Error)
            had_a_step_to_run = executeNextStep();

        return finishTest();
        }

    public boolean startTest()
        {
        _context.raiseEvent(new StartTestEvent(_test));
        _test_status = MuseTestResultStatus.Success;

        if (!_test.initializeContext(_context))
            {
            _test_status = MuseTestResultStatus.Error;
            return false;
            }

        _context.getExecutionStack().push(new SingleStepExecutionContext(_context, _test.getStep(), true));

        return true;
        }

    public MuseTestResult finishTest()
        {
        MuseTestResult result = new BaseMuseTestResult(_test_status, _test, _log);
        _context.raiseEvent(new EndTestEvent(_test, result));
        _context.cleanup();
        return result;
        }

    public void setTestStatus(MuseTestResultStatus status)
        {
        _test_status = status;
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

            if (step_result.getStatus() == StepExecutionStatus.FAILURE && _test_status == MuseTestResultStatus.Success)
                _test_status = MuseTestResultStatus.Failure;
            else if (step_result.getStatus() == StepExecutionStatus.ERROR)
                _test_status = MuseTestResultStatus.Error;
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
            StepConfiguration config = null;
            if (step != null)
                {
                config = step.getConfiguration();
                _context.raiseEvent(new StepEvent(MuseEventType.EndStep, config, step_context, step_result));
                }
            _test_status = MuseTestResultStatus.Error;
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
    private final EventLog _log = new EventLog();
    private MuseTestResultStatus _test_status;

    final static Logger LOG = LoggerFactory.getLogger(SteppedTestExecutor.class);
    }


