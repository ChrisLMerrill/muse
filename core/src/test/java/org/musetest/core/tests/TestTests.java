package org.musetest.core.tests;

import org.junit.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Test;
import org.musetest.builtins.step.*;
import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.events.*;
import org.musetest.core.events.matching.*;
import org.musetest.core.project.*;
import org.musetest.core.step.*;
import org.musetest.core.steptest.*;
import org.musetest.core.tests.utils.*;
import org.musetest.core.values.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class TestTests
    {
    @Test
    public void eventGeneration()
	    {
        SteppedTest test = setupLogTest(null);
	    final TestExecutionContext context = TestRunHelper.runTestReturnContext(new SimpleProject(), test);
	    TestResult result = TestResult.find(context);
	    Assertions.assertNotNull(result);
        Assertions.assertTrue(result.isPass());
        Assertions.assertEquals(1, context.getEventLog().findEvents(new EventTypeMatcher(StartTestEventType.TYPE_ID)).size());
        Assertions.assertEquals(1, context.getEventLog().findEvents(new EventTypeMatcher(EndTestEventType.TYPE_ID)).size());
        }

    @Test
    public void stepSetupFailure()
        {
        SteppedTest test = setupLogTest(null);
        test.getStep().removeSource(LogMessage.MESSAGE_PARAM);
        final TestExecutionContext context = TestRunHelper.runTestReturnContext(new SimpleProject(), test);
        TestResult result = TestResult.find(context);

        Assertions.assertNotNull(result);
        Assertions.assertFalse(result.isPass());
        }

    @Test
    public void stopOnError()
        {
        StepConfiguration step = new StepConfiguration(Verify.TYPE_ID); // will cause an error - condition param is missing
        SteppedTest test = setupLogTest(step);

        final TestExecutionContext context = TestRunHelper.runTestReturnContext(new SimpleProject(), test);
        TestResult result = TestResult.find(context);
        Assertions.assertNotNull(result);
        Assertions.assertFalse(result.isPass());  // test marked as failure

        // should stop after verify step
        Assertions.assertTrue(context.getEventLog().findEvents(new EventTypeMatcher(StartStepEventType.TYPE_ID)).size() == 2);
        }

    @Test
    public void continueOnVerifyFailure()
        {
        StepConfiguration step = new StepConfiguration(Verify.TYPE_ID);
        step.addSource(Verify.CONDITION_PARAM, ValueSourceConfiguration.forValue(false)); // will cause a failure
        SteppedTest test = setupLogTest(step);

        final TestExecutionContext context = TestRunHelper.runTestReturnContext(new SimpleProject(), test);
        TestResult result = TestResult.find(context);
        Assertions.assertNotNull(result);
        Assertions.assertFalse(result.isPass());  // test marked as failure

        // should run all steps
        Assertions.assertTrue(context.getEventLog().findEvents(new EventTypeMatcher(StartStepEventType.TYPE_ID)).size() == 3);
        }

    @Test
    public void stopOnVerifyWithTerminateOption()
        {
        StepConfiguration step = new StepConfiguration(Verify.TYPE_ID);
        step.addSource(Verify.CONDITION_PARAM, ValueSourceConfiguration.forValue(false)); // will cause a failure
        step.addSource(Verify.TERMINATE_PARAM, ValueSourceConfiguration.forValue(true)); // should cause test to stop
        SteppedTest test = setupLogTest(step);

        final TestExecutionContext context = TestRunHelper.runTestReturnContext(new SimpleProject(), test);
        TestResult result = TestResult.find(context);
        Assertions.assertNotNull(result);  // test did not pass
        Assertions.assertFalse(result.isPass());  // test did not pass
        Assertions.assertEquals(TestResult.FailureType.Failure, result.getFailures().get(0).getType());

        // should stop after verify step
        Assertions.assertTrue(context.getEventLog().findEvents(new EventTypeMatcher(StartStepEventType.TYPE_ID)).size() == 2);
        }

    /**
     * An event that has failure status causes the test result to have failure status
     */
    @Test
    public void failureEventStatusCausesTestFailureStatus()
        {
        MuseEvent event = new MuseEvent(MessageEventType.INSTANCE);
        event.addTag(MuseEvent.FAILURE);
        runEventRaisingTest(event);

        Assertions.assertFalse(_result.isPass());  // test did not pass
        Assertions.assertEquals(TestResult.FailureType.Failure, _result.getFailures().get(0).getType());

        // it was not fatal, so all steps should run
        Assertions.assertTrue(_context.getEventLog().findEvents(new EventTypeMatcher(StartStepEventType.TYPE_ID)).size() == 3);
        }

    /**
     * An event that has error status causes the test result to have error status. And test Terminates
     */
    @Test
    public void errorEventStatusCausesTestErrorStatus()
        {
        MuseEvent event = new MuseEvent(MessageEventType.INSTANCE);
        event.addTag(MuseEvent.ERROR);
        runEventRaisingTest(event);

        Assertions.assertFalse(_result.isPass());  // test did not pass
        Assertions.assertEquals(TestResult.FailureType.Error, _result.getFailures().get(0).getType());
        }

    /**
     * An event that has error status causes the test result to have error status. And test Terminates
     */
    @Test
    public void eventTerminatePropertyCausesTermination()
        {
        MuseEvent event = new MuseEvent(MessageEventType.INSTANCE);
        event.addTag(MuseEvent.TERMINATE);
        runEventRaisingTest(event);

        // second step should not run (technically 3rd, since the 2 are contained in compound step)
        Assertions.assertTrue(_context.getEventLog().findEvents(new EventTypeMatcher(StartStepEventType.TYPE_ID)).size() == 2);
        }

    private void runEventRaisingTest(MuseEvent event)
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
				    protected StepExecutionResult executeImplementation(StepExecutionContext context)
					    {
					    context.raiseEvent(event);
					    return new BasicStepExecutionResult(StepExecutionStatus.COMPLETE);
					    }
				    };
			    }
		    };
	    SteppedTest test = setupLogTest(step);

	    _context = TestRunHelper.runTestReturnContext(new SimpleProject(), test);
	    _result = TestResult.find(_context);
	    }

    private static SteppedTest setupLogTest(StepConfiguration first_step)
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

        return new SteppedTest(main_step);
        }

    private TestResult _result = null;
    private TestExecutionContext _context = null;
    }
