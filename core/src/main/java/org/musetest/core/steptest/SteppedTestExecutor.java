package org.musetest.core.steptest;

import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.events.*;
import org.musetest.core.step.*;
import org.musetest.core.test.*;
import org.slf4j.*;

import java.util.*;

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
        context.pushProvider(new SimpleStepConfigProvider(_test.getStep()));
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
        StepConfiguration step_config = getNextStep();
        if (step_config == null)
            return false;

        StepExecutionContext step_context = null;
        String error_message;
        try
            {
            error_message = null;
            step_context = _step_contexts.get(step_config);
            if (step_context == null)
                step_context = createStepExecutionContext(step_config);
            _context.raiseEvent(new StepEvent(MuseEventType.StartStep, step_config, step_context));

            MuseStep step = step_config.createStep(_context.getProject());

            StepExecutionResult step_result = step.execute(step_context);
            if (step_result.getStatus() == StepExecutionStatus.FAILURE)
                {
                _test_status = MuseTestResultStatus.Failure;
                finishStep(step_config, step_config, step_context, step_result);
                }
            else if (step_result.getStatus() == StepExecutionStatus.ERROR)
                {
                _test_status = MuseTestResultStatus.Error;
                finishStep(step_config, step_config, step_context, step_result);
                }
            else if (step_result.getStatus() == StepExecutionStatus.COMPLETE || step_result.getStatus() == StepExecutionStatus.RETURN)
                {
                if (step_result.getStatus() == StepExecutionStatus.RETURN)
                    LOG.info("info");
                finishStep(step_config, step_config, step_context, step_result);
                }
            else if (step_result.getStatus() == StepExecutionStatus.INCOMPLETE)
                {
                _context.raiseEvent(new StepEvent(MuseEventType.EndStep, step_config, step_context, step_result));
                if (step instanceof CompoundStep)
                    _context.pushProvider(((CompoundStep)step).getStepProvider(step_context, step_config));
                else
                    throw new StepConfigurationError("A step returned an INCOMPLETE status, but does not implement the CompoundStep interface: " + step.getClass().getName());
                }
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
            LOG.error("Unexpected error caught while executing step " + step_config.toString(), t);
            error_message = "cannot execute the step due to an unexpected error: " + t.getMessage();
            }

        if (error_message != null)
            {
            StepExecutionResult result = new BasicStepExecutionResult(StepExecutionStatus.ERROR, error_message);
            if (step_context != null) // if the step was started
                finishStep(step_config, step_config, step_context, result);
            _context.raiseEvent(new TestErrorEvent(error_message));
            _test_status = MuseTestResultStatus.Error;
            }

        return true;
        }

    public StepConfiguration getNextStep()
        {
        return _context.getStepConfigProvider().queryCurrentStep();
        }

    private void finishStep(StepConfiguration step_config, StepConfiguration config, StepExecutionContext context, StepExecutionResult step_result)
        {
        _context.raiseEvent(new StepEvent(MuseEventType.EndStep, config, context, step_result));
        removeStepExecutionContext(step_config);
        _context.getStepConfigProvider().popCurrentStep();
        }

    private StepExecutionContext createStepExecutionContext(StepConfiguration configuration)
        {
        StepExecutionContext context = new SimpleStepExecutionContext(_context);
        _step_contexts.put(configuration, context);
        return context;
        }

    private void removeStepExecutionContext(StepConfiguration configuration)
        {
        _step_contexts.remove(configuration);
        }

    private Map<StepConfiguration, StepExecutionContext> _step_contexts = new HashMap<>();
    private SteppedTestExecutionContext _context;
    private SteppedTest _test;
    private final EventLog _log = new EventLog();
    private MuseTestResultStatus _test_status;

    final static Logger LOG = LoggerFactory.getLogger(SteppedTestExecutor.class);
    }


