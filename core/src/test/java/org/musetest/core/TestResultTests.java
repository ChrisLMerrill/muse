package org.musetest.core;

import org.jetbrains.annotations.*;
import org.junit.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Test;
import org.musetest.core.events.*;
import org.musetest.core.execution.*;
import org.musetest.core.mocks.*;
import org.musetest.core.step.*;
import org.musetest.core.steptest.*;
import org.musetest.core.test.plugins.*;
import org.musetest.core.values.*;

import java.io.*;
import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class TestResultTests
	{
	@Test
	public void equality()
	    {
	    TestResult result1 = createResult();
	    TestResult result2 = createResult();

	    Assertions.assertTrue(result1.equals(result2));
	    }

	@Test
	public void serialization() throws IOException
		{
	    TestResult result = createResult();
	    ByteArrayOutputStream outstream = new ByteArrayOutputStream();
	    result.write(outstream);
	    ByteArrayInputStream instream = new ByteArrayInputStream(outstream.toByteArray());
	    TestResult copy = new TestResult().read(instream);

		Assertions.assertTrue(copy.equals(result));
	    }

	@NotNull
	private TestResult createResult()
		{
		TestResult result = new TestResult();
		result.setName("result1");
		result.setPass(true);
		result.setSummary("summary of result");
		result.setTestId("test1");
		List<TestResult.Failure> failures = new ArrayList<>();
		failures.add(new TestResult.Failure(TestResult.FailureType.Failure, "failure 1"));
		failures.add(new TestResult.Failure(TestResult.FailureType.Error, "error 1"));
		failures.add(new TestResult.Failure(TestResult.FailureType.Interrupted, "halted by user"));
		result.setFailures(failures);
		return result;
		}

	@Test
	public void collectSuccessfulResult()
		{
	    _context.raiseEvent(EndStepEventType.create(_step, new MockStepExecutionContext(), new BasicStepExecutionResult(StepExecutionStatus.COMPLETE)));

		Assertions.assertEquals(1, _collector.getData().size());
	    Assertions.assertTrue(_collector.getResult().isPass());
	    Assertions.assertEquals(0, _collector.getResult().getFailures().size());
	    }

	@Test
	public void collectDetails() throws MuseExecutionError
		{
	    TestResultCollector collector = new TestResultCollector(new TestResultCollectorConfiguration());
	    final MockMuseExecutionContext context = new MockMuseExecutionContext();
	    collector.initialize(context);
		context.raiseEvent(StartTestEventType.create(_test.getId(), "test name"));

		Assertions.assertEquals(1, _collector.getData().size());
	    Assertions.assertEquals("test name", collector.getResult().getName());
	    Assertions.assertEquals("test1", collector.getResult().getTestId());
	    }

	@Test
	public void collectFailureResult()
		{
	    _context.raiseEvent(EndStepEventType.create(_step, new MockStepExecutionContext(), new BasicStepExecutionResult(StepExecutionStatus.FAILURE, "failed the test")));

		Assertions.assertEquals(1, _collector.getData().size());
		Assertions.assertEquals(1, _collector.getResult().getFailures().size());
	    Assertions.assertFalse(_collector.getResult().isPass());
	    }

	@Test
	public void collectErrorResult()
		{
	    _context.raiseEvent(EndStepEventType.create(_step, new MockStepExecutionContext(), new BasicStepExecutionResult(StepExecutionStatus.ERROR, "failed the test")));

		Assertions.assertEquals(1, _collector.getData().size());
	    Assertions.assertFalse(_collector.getResult().isPass());
	    Assertions.assertEquals(1, _collector.getResult().getFailures().size());
	    }

	@Test
	public void collectMultipleFailures()
		{
	    _context.raiseEvent(EndStepEventType.create(_step, new MockStepExecutionContext(), new BasicStepExecutionResult(StepExecutionStatus.COMPLETE)));
	    _context.raiseEvent(EndStepEventType.create(_step, new MockStepExecutionContext(), new BasicStepExecutionResult(StepExecutionStatus.FAILURE, "failed the test")));
	    _context.raiseEvent(EndStepEventType.create(_step, new MockStepExecutionContext(), new BasicStepExecutionResult(StepExecutionStatus.ERROR, "failed the test")));

		Assertions.assertEquals(1, _collector.getData().size());
	    Assertions.assertFalse(_collector.getResult().isPass());
	    Assertions.assertEquals(2, _collector.getResult().getFailures().size());
	    }

	@Test
	public void honorFailOnErrorSetting() throws MuseExecutionError
		{
	    _collector = createCollector(false, null, null);
	    _collector.initialize(_context);
	    _context.raiseEvent(EndStepEventType.create(_step, new MockStepExecutionContext(), new BasicStepExecutionResult(StepExecutionStatus.ERROR, "failed the test")));

		Assertions.assertEquals(1, _collector.getData().size());
	    Assertions.assertTrue(_collector.getResult().isPass());
	    Assertions.assertEquals(0, _collector.getResult().getFailures().size());
	    }

	@Test
	public void honorFailOnFailureSetting() throws MuseExecutionError
		{
	    _collector = createCollector(null, false, null);
	    _collector.initialize(_context);
		_context.raiseEvent(EndStepEventType.create(_step, new MockStepExecutionContext(), new BasicStepExecutionResult(StepExecutionStatus.FAILURE, "failed the test")));

		Assertions.assertEquals(1, _collector.getData().size());
	    Assertions.assertTrue(_collector.getResult().isPass());
	    Assertions.assertEquals(0, _collector.getResult().getFailures().size());
	    }

	@Test
	public void honorFailOnInterruptSetting() throws MuseExecutionError
		{
	    _collector = createCollector(null, null, false);
	    _collector.initialize(_context);
		_context.raiseEvent(new MuseEvent(new InterruptedEventType()));

		Assertions.assertEquals(1, _collector.getData().size());
	    Assertions.assertTrue(_collector.getResult().isPass());
	    Assertions.assertEquals(0, _collector.getResult().getFailures().size());
	    }

	private TestResultCollector createCollector(Boolean fail_on_error, Boolean fail_on_failure, Boolean fail_on_interrupt)
		{
		final TestResultCollectorConfiguration configuration = new TestResultCollectorConfiguration();
		if (fail_on_error != null)
			configuration.parameters().addSource(TestResultCollectorConfiguration.FAIL_ON_ERROR, ValueSourceConfiguration.forValue(fail_on_error));
		if (fail_on_failure != null)
			configuration.parameters().addSource(TestResultCollectorConfiguration.FAIL_ON_FAILURE, ValueSourceConfiguration.forValue(fail_on_failure));
		if (fail_on_interrupt != null)
			configuration.parameters().addSource(TestResultCollectorConfiguration.FAIL_ON_INTERRUPT, ValueSourceConfiguration.forValue(fail_on_interrupt));
		return new TestResultCollector(configuration);
		}

	@BeforeEach
	public void setup() throws MuseExecutionError
		{
		_collector = createCollector(null, null, null);
		_context = new MockMuseExecutionContext();
	    _collector.initialize(_context);
		_step = new StepConfiguration("step-type");
		_step.setStepId(99L);
		_test = new SteppedTest(_step);
		_test.setId("test1");
		}

	private TestResultCollector _collector;
	private MockMuseExecutionContext _context;
	private StepConfiguration _step;
	private SteppedTest _test;
	}
