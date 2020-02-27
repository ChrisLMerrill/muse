package org.museautomation.core.step;

import org.museautomation.core.*;
import org.museautomation.core.context.*;
import org.museautomation.core.events.*;
import org.museautomation.core.execution.*;
import org.museautomation.core.steptask.*;
import org.museautomation.core.values.*;
import org.slf4j.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class StepExecutor
    {
    public StepExecutor(ContainsStep step, StepsExecutionContext context)
        {
        _context = context;
        _context.getExecutionStack().push(new SingleStepExecutionContext(_context, step.getStep(), true));
        }

    public void executeAll()
        {
        boolean had_a_step_to_run = true;
        while (had_a_step_to_run && !isTerminateRequested())
            had_a_step_to_run = executeNextStep();
        }

    /**
     * @return false if there are no more steps to run.
     */
    public boolean executeNextStep()
        {
        if (!_context.getExecutionStack().hasMoreSteps())
            return false;

        if (Thread.currentThread().isInterrupted())
            {
            _context.raiseEvent(new MuseEvent(InterruptedEventType.TYPE));
            return false;
            }

        String error_message = null;
        StepExecutionResult step_result = null;
        MuseStep step = null;

        StepExecutionContext step_context = _context.getExecutionStack().peek();
        StepConfiguration step_config = step_context.getCurrentStepConfiguration();
        if (!(_steps_in_progress.contains(step_config)))
            {
            _steps_in_progress.add(step_config);
            _context.raiseEvent(StartStepEventType.create(step_config, step_context));
            }
        try
            {
            step = step_context.getCurrentStep();
            step_result = step.execute(step_context);
            }
        catch (StepConfigurationError error)
            {
            error_message = "step error due to configuration problem: " + error.getMessage();
            _context.raiseEvent(TaskErrorEventType.create("Step configuration problem: " + error.getMessage()));
            }
        catch (StepExecutionError error)
            {
            error_message = "step failed to execute: " + error.getMessage();
            _context.raiseEvent(TaskErrorEventType.create("Step execution problem: " + error.getMessage()));
            }
        catch (ValueSourceResolutionError error)
	        {
	        error_message = "unable to evalute value source: " + error.getMessage();
            _context.raiseEvent(TaskErrorEventType.create("Unable to resolve value source: " + error.getMessage()));
	        }
        catch (Throwable t)
            {
            LOG.error("Unexpected error caught while executing step", t);
            error_message = "cannot execute the step due to an unexpected error: " + t.getMessage();
            }

        if (error_message != null) // if the step did not complete normally, the EndStep event needs to be generated here, since it was not generated by the step itself.
            step_result = new BasicStepExecutionResult(StepExecutionStatus.ERROR, error_message);
        if (step != null && !step_result.getStatus().equals(StepExecutionStatus.INCOMPLETE))
            step_context.stepComplete(step, step_result);
        _context.raiseEvent(EndStepEventType.create(step_config, step_context, step_result));

        if (!step_result.getStatus().equals(StepExecutionStatus.INCOMPLETE))
            _steps_in_progress.remove(step_config);

        return !_steps_in_progress.isEmpty();
        }

    public StepConfiguration getNextStep()
        {
        if (_context.getExecutionStack().hasMoreSteps())
            return _context.getExecutionStack().peek().getCurrentStepConfiguration();
        return null;
        }

    @SuppressWarnings("WeakerAccess")  // used in GUI
    public void requestTerminate()
        {
        _terminate = true;
        }

    public boolean isTerminateRequested()
        {
        return _terminate;
        }

    private StepsExecutionContext _context;
    private boolean _terminate = false;
    private Set<StepConfiguration> _steps_in_progress = new HashSet<>();

    private final static Logger LOG = LoggerFactory.getLogger(StepExecutor.class);
    }