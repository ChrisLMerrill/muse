package org.musetest.core.tests;

import org.junit.*;
import org.musetest.builtins.step.*;
import org.musetest.core.*;
import org.musetest.core.events.*;
import org.musetest.core.project.*;
import org.musetest.core.step.*;
import org.musetest.core.test.*;
import org.musetest.core.tests.mocks.*;
import org.musetest.core.variables.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class TestResultProducerTests
    {
    @Test
    public void basics()
        {
        EventLog log = new EventLog();
        MuseTest test = new MockTest();
        TestResultProducer producer = new DefaultTestResultProducer(test, log);

        MuseTestResult result = producer.getTestResult();
        Assert.assertTrue(result.isPass());
        Assert.assertNull(result.getFailureDescription());
        Assert.assertEquals(log, result.getLog());
        Assert.assertEquals(test, result.getTest());
        Assert.assertTrue(result.getOneLineDescription().toLowerCase().contains("success"));
        }

    @Test
    public void stepFailure()
        {
        EventLog log = new EventLog();
        MuseTest test = new MockTest();
        TestResultProducer producer = new DefaultTestResultProducer(test, log);

        String reason = "test123";
        producer.eventRaised(new StepEvent(MuseEventType.EndStep, new StepConfiguration(LogMessage.TYPE_ID), new DummyStepExecutionContext(new SimpleProject()), new BasicStepExecutionResult(StepExecutionStatus.FAILURE, reason)));

        MuseTestResult result = producer.getTestResult();
        Assert.assertFalse(result.isPass());
        Assert.assertNotNull(result.getFailureDescription());
        Assert.assertEquals(MuseTestFailureDescription.FailureType.Failure, result.getFailureDescription().getFailureType());
        Assert.assertTrue(result.getOneLineDescription().toLowerCase().contains(reason));
        }

    @Test
    public void stepError()
        {
        EventLog log = new EventLog();
        MuseTest test = new MockTest();
        TestResultProducer producer = new DefaultTestResultProducer(test, log);

        String reason = "test123";
        producer.eventRaised(new StepEvent(MuseEventType.EndStep, new StepConfiguration(LogMessage.TYPE_ID), new DummyStepExecutionContext(new SimpleProject()), new BasicStepExecutionResult(StepExecutionStatus.ERROR, reason)));

        MuseTestResult result = producer.getTestResult();
        Assert.assertFalse(result.isPass());
        Assert.assertNotNull(result.getFailureDescription());
        Assert.assertEquals(MuseTestFailureDescription.FailureType.Error, result.getFailureDescription().getFailureType());
        Assert.assertTrue(result.getOneLineDescription().toLowerCase().contains(reason));
        }

    @Test
    public void verifyFailed()
        {
        EventLog log = new EventLog();
        MuseTest test = new MockTest();
        TestResultProducer producer = new DefaultTestResultProducer(test, log);

        String reason = "verifyfail";
        producer.eventRaised(new VerifyFailureEvent(new StepConfiguration(LogMessage.TYPE_ID), new DummyStepExecutionContext(new SimpleProject()), reason));

        MuseTestResult result = producer.getTestResult();
        Assert.assertFalse(result.isPass());
        Assert.assertNotNull(result.getFailureDescription());
        Assert.assertEquals(MuseTestFailureDescription.FailureType.Failure, result.getFailureDescription().getFailureType());
        Assert.assertTrue(result.getOneLineDescription().toLowerCase().contains(reason));
        }
    }

