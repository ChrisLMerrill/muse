package org.musetest.core.tests;

import org.junit.*;
import org.musetest.builtins.step.*;
import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.events.*;
import org.musetest.core.step.*;
import org.musetest.core.steptest.*;
import org.musetest.core.values.*;
import org.musetest.core.variables.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class StepExecutionTests
    {
    @Test
    public void singleStep()
        {
        final String message = "this is the message";
        StepConfiguration step_a = new StepConfiguration(LogMessage.TYPE_ID);
        step_a.setSource(LogMessage.MESSAGE_PARAM, ValueSourceConfiguration.forValue(message));

        SteppedTest test = new SteppedTest(step_a);
        DefaultTestExecutionContext test_context = new DefaultTestExecutionContext();
        EventLog log = new EventLog();
        test_context.addEventListener(log);
        SteppedTestExecutor executor = new SteppedTestExecutor(test, new DefaultSteppedTestExecutionContext(test_context));

        MuseTestResult result = executor.executeAll();

        Assert.assertTrue(result.isPass());
        Assert.assertTrue("message step did not run", log.hasEventWithDescriptionContaining(message));
        }

    @Test
    public void singleNestedStep()
        {
        StepConfiguration parent = new StepConfiguration();
        parent.setType("compound");

        final String message = "the message";
        StepConfiguration child = new StepConfiguration(LogMessage.TYPE_ID);
        child.setSource(LogMessage.MESSAGE_PARAM, ValueSourceConfiguration.forValue(message));
        parent.addChild(child);

        SteppedTest test = new SteppedTest(parent);
        DefaultTestExecutionContext test_context = new DefaultTestExecutionContext();
        EventLog log = new EventLog();
        test_context.addEventListener(log);
        SteppedTestExecutor executor = new SteppedTestExecutor(test, new DefaultSteppedTestExecutionContext(test_context));

        MuseTestResult result = executor.executeAll();

        Assert.assertTrue(result.isPass());
        Assert.assertTrue("step didn't run", log.hasEventWithDescriptionContaining(message));
        }

    @Test
    public void twoNestedSteps()
        {
        StepConfiguration parent = new StepConfiguration();
        parent.setType("compound");

        final String message1 = "message1";
        StepConfiguration child1 = new StepConfiguration(LogMessage.TYPE_ID);
        child1.setSource(LogMessage.MESSAGE_PARAM, ValueSourceConfiguration.forValue(message1));
        parent.addChild(child1);

        final String message2 = "message2";
        StepConfiguration child2 = new StepConfiguration(LogMessage.TYPE_ID);
        child2.setSource(LogMessage.MESSAGE_PARAM, ValueSourceConfiguration.forValue(message2));
        parent.addChild(child2);

        SteppedTest test = new SteppedTest(parent);
        DefaultTestExecutionContext test_context = new DefaultTestExecutionContext();
        EventLog log = new EventLog();
        test_context.addEventListener(log);
        SteppedTestExecutor executor = new SteppedTestExecutor(test, new DefaultSteppedTestExecutionContext(test_context));

        MuseTestResult result = executor.executeAll();

        Assert.assertTrue(result.isPass());
        Assert.assertTrue("first step didn't run", log.hasEventWithDescriptionContaining(message1));
        Assert.assertTrue("second step didn't run", log.hasEventWithDescriptionContaining(message2));
        }

    // TODO test doubly-nested compound steps

    // TODO test step failure - e.g. verify(false)

    @Test
    public void stepMissingParameter()
        {
        StepConfiguration step_a = new StepConfiguration();
        step_a.setType("blahblah");
        SteppedTest test = new SteppedTest(step_a);
        DefaultTestExecutionContext test_context = new DefaultTestExecutionContext();
        SteppedTestExecutor executor = new SteppedTestExecutor(test, new DefaultSteppedTestExecutionContext(test_context));

        MuseTestResult result = executor.executeAll();
        Assert.assertEquals(MuseTestFailureDescription.FailureType.Error, result.getFailureDescription().getFailureType());
        }

    @Test
    public void stepParameterResolvesToNull()
        {
        StepConfiguration step_a = new StepConfiguration();
        step_a.setType("blahblah");
        step_a.setSource(StoreVariable.NAME_PARAM, ValueSourceConfiguration.forValue(null));

        SteppedTest test = new SteppedTest(step_a);
        DefaultTestExecutionContext test_context = new DefaultTestExecutionContext();
        SteppedTestExecutor executor = new SteppedTestExecutor(test, new DefaultSteppedTestExecutionContext(test_context));

        MuseTestResult result = executor.executeAll();
        Assert.assertEquals(MuseTestFailureDescription.FailureType.Error, result.getFailureDescription().getFailureType());
        }


    }


