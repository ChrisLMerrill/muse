package org.musetest.core.tests;

import org.junit.*;
import org.musetest.builtins.step.*;
import org.musetest.core.*;
import org.musetest.core.events.*;
import org.musetest.core.mocks.*;
import org.musetest.core.project.*;
import org.musetest.core.step.*;
import org.musetest.core.test.*;
import org.musetest.core.variables.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class TestResultProducerTests
    {
    @Test
    public void basics() throws MuseExecutionError
		{
		final MockTest test = new MockTest();
		MockSteppedTestExecutionContext context = new MockSteppedTestExecutionContext(test);
        context.runInitializers();  // need to do this manually since we're not actually running a test
        TestResultProducer producer = new TestFailsOnErrorFailureOrInterrupt(test, context);

        MuseTestResult result = producer.getTestResult();
        Assert.assertTrue(result.isPass());
        Assert.assertNull(result.getFailureDescription());
		Assert.assertNotNull(result.getLog());
        Assert.assertEquals(test, result.getTest());
        Assert.assertTrue(result.getOneLineDescription().toLowerCase().contains("success"));
        }

    @Test
    public void stepFailure()
        {
        MuseTest test = new MockTest();
		MockSteppedTestExecutionContext context = new MockSteppedTestExecutionContext();
		context.addInitializer(new EventLogger());
        TestResultProducer producer = new TestFailsOnErrorFailureOrInterrupt(test, context);

        String reason = "test123";
        producer.eventRaised(new StepEvent(StepEvent.END_TYPE, new StepConfiguration(LogMessage.TYPE_ID), new MockStepExecutionContext(new SimpleProject()), new BasicStepExecutionResult(StepExecutionStatus.FAILURE, reason)));

        MuseTestResult result = producer.getTestResult();
        Assert.assertFalse(result.isPass());
        Assert.assertNotNull(result.getFailureDescription());
        Assert.assertEquals(MuseTestFailureDescription.FailureType.Failure, result.getFailureDescription().getFailureType());
        Assert.assertTrue(result.getOneLineDescription().toLowerCase().contains(reason));
        }

    @Test
    public void stepError()
        {
        MuseTest test = new MockTest();
		MockSteppedTestExecutionContext context = new MockSteppedTestExecutionContext();
		context.addInitializer(new EventLogger());
        TestResultProducer producer = new TestFailsOnErrorFailureOrInterrupt(test, context);

        String reason = "test123";
        producer.eventRaised(new StepEvent(StepEvent.END_TYPE, new StepConfiguration(LogMessage.TYPE_ID), new MockStepExecutionContext(new SimpleProject()), new BasicStepExecutionResult(StepExecutionStatus.ERROR, reason)));

        MuseTestResult result = producer.getTestResult();
        Assert.assertFalse(result.isPass());
        Assert.assertNotNull(result.getFailureDescription());
        Assert.assertEquals(MuseTestFailureDescription.FailureType.Error, result.getFailureDescription().getFailureType());
        Assert.assertTrue(result.getOneLineDescription().toLowerCase().contains(reason));
        }

    @Test
    public void verifyFailed()
        {
        MuseTest test = new MockTest();
		MockSteppedTestExecutionContext context = new MockSteppedTestExecutionContext();
		context.addInitializer(new EventLogger());
        TestResultProducer producer = new TestFailsOnErrorFailureOrInterrupt(test, context);

        String reason = "verifyfail";
        producer.eventRaised(new VerifyFailureEvent(new StepConfiguration(LogMessage.TYPE_ID), new MockStepExecutionContext(new SimpleProject()), reason));

        MuseTestResult result = producer.getTestResult();
        Assert.assertFalse(result.isPass());
        Assert.assertNotNull(result.getFailureDescription());
        Assert.assertEquals(MuseTestFailureDescription.FailureType.Failure, result.getFailureDescription().getFailureType());
        Assert.assertTrue(result.getOneLineDescription().toLowerCase().contains(reason));
        }
    }


