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
import org.musetest.core.steptest.SteppedTest;
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
        MuseTestResult result = test.execute(new DefaultTestExecutionContext(new SimpleProject(), test));

        Assert.assertTrue(result.isPass());
        Assert.assertEquals(1, result.getLog().findEvents(new EventTypeMatcher(MuseEventType.StartTest)).size());
        Assert.assertEquals(1, result.getLog().findEvents(new EventTypeMatcher(MuseEventType.EndTest)).size());
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

        EventLog log = new EventLog();
        DefaultTestExecutionContext test_context = new DefaultTestExecutionContext(new SimpleProject(), test);
        test_context.addEventListener(log);

        MuseTestResult result = test.execute(test_context);
        Assert.assertFalse(result.isPass());  // test marked as failure

        // should stop after verify step
        Assert.assertTrue(log.findEvents(new EventTypeMatcher(MuseEventType.StartStep)).size() == 2);
        }

    @Test
    public void continueOnVerifyFailure()
        {
        StepConfiguration step = new StepConfiguration(Verify.TYPE_ID);
        step.addSource(Verify.CONDITION_PARAM, ValueSourceConfiguration.forValue(false)); // will cause a failure
        SteppedTest test = setupLogTest(step);

        EventLog log = new EventLog();
        DefaultTestExecutionContext test_context = new DefaultTestExecutionContext(new SimpleProject(), test);
        test_context.addEventListener(log);

        MuseTestResult result = test.execute(test_context);
        Assert.assertFalse(result.isPass());  // test marked as failure

        // should run all steps
        Assert.assertTrue(log.findEvents(new EventTypeMatcher(MuseEventType.StartStep)).size() == 3);
        }

    @Test
    public void stopOnVerifyWithTerminateOption()
        {
        StepConfiguration step = new StepConfiguration(Verify.TYPE_ID);
        step.addSource(Verify.CONDITION_PARAM, ValueSourceConfiguration.forValue(false)); // will cause a failure
        step.addSource(Verify.TERMINATE_PARAM, ValueSourceConfiguration.forValue(true)); // should cause test to stop
        SteppedTest test = setupLogTest(step);

        EventLog log = new EventLog();
        DefaultTestExecutionContext test_context = new DefaultTestExecutionContext(new SimpleProject(), test);
        test_context.addEventListener(log);

        MuseTestResult result = test.execute(test_context);
        Assert.assertFalse(result.isPass());  // test marked as failure

        // should stop after verify step
        Assert.assertTrue(log.findEvents(new EventTypeMatcher(MuseEventType.StartStep)).size() == 2);
        }

    private SteppedTest setupLogTest(StepConfiguration first_step)
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


