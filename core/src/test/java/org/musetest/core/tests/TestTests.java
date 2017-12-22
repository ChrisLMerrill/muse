package org.musetest.core.tests;

import org.junit.*;
import org.musetest.builtins.step.*;
import org.musetest.builtins.value.*;
import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.events.*;
import org.musetest.core.events.matching.*;
import org.musetest.core.project.*;
import org.musetest.core.step.*;
import org.musetest.core.steptest.*;
import org.musetest.core.values.*;
import org.musetest.core.variables.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class TestTests
    {
    @Test
    public void eventGeneration()
        {
        SteppedTest test = setupLogTest(null);
		final DefaultTestExecutionContext context = new DefaultTestExecutionContext(new SimpleProject(), test);
		context.addTestPlugin(new EventLogger());
		MuseTestResult result = test.execute(context);

        Assert.assertTrue(result.isPass());
        Assert.assertEquals(1, result.getLog().findEvents(new EventTypeMatcher(new StartTestEvent.StartTestEventType().getTypeId())).size());
        Assert.assertEquals(1, result.getLog().findEvents(new EventTypeMatcher(new EndTestEvent.EndTestEventType().getTypeId())).size());
        }

    @Test
    public void initializationFailure()
        {
        SteppedTest test = setupLogTest(null);
        test.setDefaultVariable("default1", ValueSourceConfiguration.forType(ProjectResourceValueSource.TYPE_ID));
        MuseTestResult result = test.execute(new DefaultTestExecutionContext(new SimpleProject(), test));

        Assert.assertFalse(result.isPass());
        }

    @Test
    public void stepSetupFailure()
        {
        SteppedTest test = setupLogTest(null);
        test.getStep().removeSource(LogMessage.MESSAGE_PARAM);
        MuseTestResult result = test.execute(new DefaultTestExecutionContext(new SimpleProject(), test));

        Assert.assertFalse(result.isPass());
        }

    @Test
    public void stopOnError()
        {
        StepConfiguration step = new StepConfiguration(Verify.TYPE_ID); // will cause an error - condition param is missing
        SteppedTest test = setupLogTest(step);

        EventLogger logger = new EventLogger();
        DefaultTestExecutionContext test_context = new DefaultTestExecutionContext(new SimpleProject(), test);
        test_context.addEventListener(logger);

        MuseTestResult result = test.execute(test_context);
        Assert.assertFalse(result.isPass());  // test marked as failure

        // should stop after verify step
        Assert.assertTrue(logger.getData().findEvents(new EventTypeMatcher(StepEvent.StartStepEventType.TYPE_ID)).size() == 2);
        }

    @Test
    public void continueOnVerifyFailure()
        {
        StepConfiguration step = new StepConfiguration(Verify.TYPE_ID);
        step.addSource(Verify.CONDITION_PARAM, ValueSourceConfiguration.forValue(false)); // will cause a failure
        SteppedTest test = setupLogTest(step);

        EventLogger logger = new EventLogger();
        DefaultTestExecutionContext test_context = new DefaultTestExecutionContext(new SimpleProject(), test);
        test_context.addEventListener(logger);

        MuseTestResult result = test.execute(test_context);
        Assert.assertFalse(result.isPass());  // test marked as failure

        // should run all steps
        Assert.assertTrue(logger.getData().findEvents(new EventTypeMatcher(StepEvent.StartStepEventType.TYPE_ID)).size() == 3);
        }

    @Test
    public void stopOnVerifyWithTerminateOption()
        {
        StepConfiguration step = new StepConfiguration(Verify.TYPE_ID);
        step.addSource(Verify.CONDITION_PARAM, ValueSourceConfiguration.forValue(false)); // will cause a failure
        step.addSource(Verify.TERMINATE_PARAM, ValueSourceConfiguration.forValue(true)); // should cause test to stop
        SteppedTest test = setupLogTest(step);

        EventLogger logger = new EventLogger();
        DefaultTestExecutionContext test_context = new DefaultTestExecutionContext(new SimpleProject(), test);
        test_context.addEventListener(logger);

        MuseTestResult result = test.execute(test_context);
        Assert.assertFalse(result.isPass());  // test did not pass
        Assert.assertEquals(MuseTestFailureDescription.FailureType.Failure, result.getFailureDescription().getFailureType());

        // should stop after verify step
        Assert.assertTrue(logger.getData().findEvents(new EventTypeMatcher(StepEvent.StartStepEventType.TYPE_ID)).size() == 2);
        }

    /**
     * An event that has failure status causes the test result to have failure status
     */
    @Test
    public void failureEventStatusCausesTestFailureStatus()
        {
        MuseEvent event = new MuseEvent(MessageEvent.MessageEventType.INSTANCE);
        event.setStatus(EventStatus.Failure);
        EventLogger logger = new EventLogger();
        MuseTestResult result = runEventRaisingTest(event, logger);


        Assert.assertFalse(result.isPass());  // test did not pass
        Assert.assertEquals(MuseTestFailureDescription.FailureType.Failure, result.getFailureDescription().getFailureType());

        // it was not fatal, so all steps should run
        Assert.assertTrue(logger.getData().findEvents(new EventTypeMatcher(StepEvent.StartStepEventType.TYPE_ID)).size() == 3);
        }

    /**
     * An event that has error status causes the test result to have error status. And test Terminates
     */
    @Test
    public void errorEventStatusCausesTestErrorStatus()
        {
        MuseEvent event = new MuseEvent(MessageEvent.MessageEventType.INSTANCE);
        event.setStatus(EventStatus.Error);
        EventLogger logger = new EventLogger();
        MuseTestResult result = runEventRaisingTest(event, logger);

        Assert.assertFalse(result.isPass());  // test did not pass
        Assert.assertEquals(MuseTestFailureDescription.FailureType.Error, result.getFailureDescription().getFailureType());
        }

    /**
     * An event that has error status causes the test result to have error status. And test Terminates
     */
    @Test
    public void eventTerminatePropertyCausesTermination()
        {
        MuseEvent event = new MuseEvent(MessageEvent.MessageEventType.INSTANCE);
        event.setTerminate(true);
        EventLogger logger = new EventLogger();
        runEventRaisingTest(event, logger);

        // second step should not run (technically 3rd, since the 2 are contained in compound step)
        Assert.assertTrue(logger.getData().findEvents(new EventTypeMatcher(StepEvent.StartStepEventType.TYPE_ID)).size() == 2);
        }

    private MuseTestResult runEventRaisingTest(MuseEvent event, EventLogger logger)
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

	    DefaultTestExecutionContext test_context = new DefaultTestExecutionContext(new SimpleProject(), test);
	    test_context.addEventListener(logger);

	    return test.execute(test_context);
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
    }


