package org.museautomation.core.tests;

import org.junit.jupiter.api.*;
import org.museautomation.builtins.step.*;
import org.museautomation.core.*;
import org.museautomation.core.context.*;
import org.museautomation.core.events.*;
import org.museautomation.core.events.matching.*;
import org.museautomation.core.project.*;
import org.museautomation.core.step.*;
import org.museautomation.core.steptask.*;
import org.museautomation.core.tests.utils.*;
import org.museautomation.core.values.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
class TaskTests
    {
    @Test
    void eventGeneration()
	    {
        SteppedTask task = setupLogTask(null);
	    final TaskExecutionContext context = TaskRunHelper.runTaskReturnContext(new SimpleProject(), task);
	    TaskResult result = TaskResult.find(context);
	    Assertions.assertNotNull(result);
        Assertions.assertTrue(result.isPass());
        Assertions.assertEquals(1, context.getEventLog().findEvents(new EventTypeMatcher(StartTaskEventType.TYPE_ID)).size());
        Assertions.assertEquals(1, context.getEventLog().findEvents(new EventTypeMatcher(EndTaskEventType.TYPE_ID)).size());
        }

    @Test
    void stepSetupFailure()
        {
        SteppedTask task = setupLogTask(null);
        task.getStep().removeSource(LogMessage.MESSAGE_PARAM);
        final TaskExecutionContext context = TaskRunHelper.runTaskReturnContext(new SimpleProject(), task);
        TaskResult result = TaskResult.find(context);

        Assertions.assertNotNull(result);
        Assertions.assertFalse(result.isPass());
        }

    @Test
    void stopOnError()
        {
        StepConfiguration step = new StepConfiguration(Verify.TYPE_ID); // will cause an error - condition param is missing
        SteppedTask task = setupLogTask(step);

        final TaskExecutionContext context = TaskRunHelper.runTaskReturnContext(new SimpleProject(), task);
        TaskResult result = TaskResult.find(context);
        Assertions.assertNotNull(result);
        Assertions.assertFalse(result.isPass());  // task marked as failure

        // should stop after verify step
        Assertions.assertEquals(2, context.getEventLog().findEvents(new EventTypeMatcher(StartStepEventType.TYPE_ID)).size());
        }

    @Test
    void continueOnVerifyFailure()
        {
        StepConfiguration step = new StepConfiguration(Verify.TYPE_ID);
        step.addSource(Verify.CONDITION_PARAM, ValueSourceConfiguration.forValue(false)); // will cause a failure
        SteppedTask task = setupLogTask(step);

        final TaskExecutionContext context = TaskRunHelper.runTaskReturnContext(new SimpleProject(), task);
        TaskResult result = TaskResult.find(context);
        Assertions.assertNotNull(result);
        Assertions.assertFalse(result.isPass());  // task marked as failure

        // should run all steps
        Assertions.assertEquals(3, context.getEventLog().findEvents(new EventTypeMatcher(StartStepEventType.TYPE_ID)).size());
        }

    @Test
    void stopOnVerifyWithTerminateOption()
        {
        StepConfiguration step = new StepConfiguration(Verify.TYPE_ID);
        step.addSource(Verify.CONDITION_PARAM, ValueSourceConfiguration.forValue(false)); // will cause a failure
        step.addSource(Verify.TERMINATE_PARAM, ValueSourceConfiguration.forValue(true)); // should cause task to stop
        SteppedTask task = setupLogTask(step);

        final TaskExecutionContext context = TaskRunHelper.runTaskReturnContext(new SimpleProject(), task);
        TaskResult result = TaskResult.find(context);
        Assertions.assertNotNull(result);  // task did not pass
        Assertions.assertFalse(result.isPass());  // task did not pass
        Assertions.assertEquals(TaskResult.FailureType.Failure, result.getFailures().get(0).getType());

        // should stop after verify step
        Assertions.assertEquals(2, context.getEventLog().findEvents(new EventTypeMatcher(StartStepEventType.TYPE_ID)).size());
        }

    /**
     * An event that has failure status causes the task result to have failure status
     */
    @Test
    void failureEventStatusCausesTaskFailureStatus()
        {
        MuseEvent event = new MuseEvent(MessageEventType.INSTANCE);
        event.addTag(MuseEvent.FAILURE);
        runEventRaisingTask(event);

        Assertions.assertFalse(_result.isPass());  // task did not pass
        Assertions.assertEquals(TaskResult.FailureType.Failure, _result.getFailures().get(0).getType());

        // it was not fatal, so all steps should run
        Assertions.assertEquals(3, _context.getEventLog().findEvents(new EventTypeMatcher(StartStepEventType.TYPE_ID)).size());
        }

    /**
     * An event that has error status causes the task result to have error status. And task Terminates
     */
    @Test
    void errorEventStatusCausesTaskErrorStatus()
        {
        MuseEvent event = new MuseEvent(MessageEventType.INSTANCE);
        event.addTag(MuseEvent.ERROR);
        runEventRaisingTask(event);

        Assertions.assertFalse(_result.isPass());  // task did not pass
        Assertions.assertEquals(TaskResult.FailureType.Error, _result.getFailures().get(0).getType());
        }

    /**
     * An event that has error status causes the task result to have error status. And task Terminates
     */
    @Test
    void eventTerminatePropertyCausesTermination()
        {
        MuseEvent event = new MuseEvent(MessageEventType.INSTANCE);
        event.addTag(MuseEvent.TERMINATE);
        runEventRaisingTask(event);

        // second step should not run (technically 3rd, since the 2 are contained in compound step)
        Assertions.assertEquals(2, _context.getEventLog().findEvents(new EventTypeMatcher(StartStepEventType.TYPE_ID)).size());
        }

    private void runEventRaisingTask(MuseEvent event)
	    {
	    // Implement a step that generates an event with failure status
	    StepConfiguration step = new StepConfiguration("mock-step")
		    {
		    @Override
		    public MuseStep createStep(MuseProject project)
			    {
			    return new BaseStep(this)
				    {
				    @Override
				    public StepExecutionResult executeImplementation(StepExecutionContext context)
					    {
					    context.raiseEvent(event);
					    return new BasicStepExecutionResult(StepExecutionStatus.COMPLETE);
					    }
				    };
			    }
		    };
	    SteppedTask task = setupLogTask(step);

	    _context = TaskRunHelper.runTaskReturnContext(new SimpleProject(), task);
	    _result = TaskResult.find(_context);
	    }

    private static SteppedTask setupLogTask(StepConfiguration first_step)
        {
        StepConfiguration log_step = new StepConfiguration(LogMessage.TYPE_ID);
        log_step.addSource(LogMessage.MESSAGE_PARAM, ValueSourceConfiguration.forValue("abc"));

        StepConfiguration main_step;
        if (first_step == null)
            main_step = log_step;
        else
            {
            main_step = new StepConfiguration(BasicCompoundStep.TYPE_ID);
            main_step.addChild(first_step);
            main_step.addChild(log_step);
            }

        return new SteppedTask(main_step);
        }

    private TaskResult _result = null;
    private TaskExecutionContext _context = null;
    }
