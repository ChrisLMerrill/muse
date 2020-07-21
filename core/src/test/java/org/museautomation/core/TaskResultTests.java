package org.museautomation.core;

import org.jetbrains.annotations.*;
import org.junit.jupiter.api.*;
import org.museautomation.builtins.plugins.results.*;
import org.museautomation.builtins.plugins.resultstorage.*;
import org.museautomation.core.events.*;
import org.museautomation.core.execution.*;
import org.museautomation.core.mocks.*;
import org.museautomation.core.step.*;
import org.museautomation.core.steptask.*;
import org.museautomation.core.values.*;

import java.io.*;
import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
class TaskResultTests
	{
	@Test
    void equality()
	    {
	    TaskResult result1 = createResult();
	    TaskResult result2 = createResult();

        Assertions.assertEquals(result1, result2);
	    }

	@Test
    void serialization() throws IOException
		{
	    TaskResult result = createResult();
	    ByteArrayOutputStream outstream = new ByteArrayOutputStream();
	    result.write(outstream);
	    ByteArrayInputStream instream = new ByteArrayInputStream(outstream.toByteArray());
	    TaskResult copy = new TaskResult().read(instream);

        Assertions.assertEquals(copy, result);
	    }

	@NotNull
	private TaskResult createResult()
		{
		TaskResult result = new TaskResult();
		result.setName("result1");
		result.setPass(true);
		result.setSummary("summary of result");
		result.setTaskId("task1");
		List<TaskResult.Failure> failures = new ArrayList<>();
		failures.add(new TaskResult.Failure(TaskResult.FailureType.Failure, "failure 1"));
		failures.add(new TaskResult.Failure(TaskResult.FailureType.Error, "error 1"));
		failures.add(new TaskResult.Failure(TaskResult.FailureType.Interrupted, "halted by user"));
		result.setFailures(failures);
		return result;
		}

	@Test
    void collectSuccessfulResult()
		{
	    _context.raiseEvent(EndStepEventType.create(_step, new MockStepExecutionContext(), new BasicStepExecutionResult(StepExecutionStatus.COMPLETE)));

		Assertions.assertEquals(1, _collector.getData().size());
	    Assertions.assertTrue(_collector.getResult().isPass());
	    Assertions.assertEquals(0, _collector.getResult().getFailures().size());
	    }

	@Test
    void collectDetails() throws MuseExecutionError
		{
	    TaskResultCollector collector = new TaskResultCollector(new TaskResultCollectorConfiguration());
	    final MockMuseExecutionContext context = new MockMuseExecutionContext();
	    collector.initialize(context);
		context.raiseEvent(StartTaskEventType.create(_task.getId(), "task name"));

		Assertions.assertEquals(1, _collector.getData().size());
	    Assertions.assertEquals("task name", collector.getResult().getName());
	    Assertions.assertEquals("task1", collector.getResult().getTaskId());
	    }

	@Test
    void collectFailureResult()
		{
	    _context.raiseEvent(EndStepEventType.create(_step, new MockStepExecutionContext(), new BasicStepExecutionResult(StepExecutionStatus.FAILURE, "failed the test")));

		Assertions.assertEquals(1, _collector.getData().size());
		Assertions.assertEquals(1, _collector.getResult().getFailures().size());
	    Assertions.assertFalse(_collector.getResult().isPass());
	    }

	@Test
    void collectErrorResult()
		{
	    _context.raiseEvent(EndStepEventType.create(_step, new MockStepExecutionContext(), new BasicStepExecutionResult(StepExecutionStatus.ERROR, "failed the task")));

		Assertions.assertEquals(1, _collector.getData().size());
	    Assertions.assertFalse(_collector.getResult().isPass());
	    Assertions.assertEquals(1, _collector.getResult().getFailures().size());
	    }

	@Test
    void collectMultipleFailures()
		{
	    _context.raiseEvent(EndStepEventType.create(_step, new MockStepExecutionContext(), new BasicStepExecutionResult(StepExecutionStatus.COMPLETE)));
	    _context.raiseEvent(EndStepEventType.create(_step, new MockStepExecutionContext(), new BasicStepExecutionResult(StepExecutionStatus.FAILURE, "failed the task")));
	    _context.raiseEvent(EndStepEventType.create(_step, new MockStepExecutionContext(), new BasicStepExecutionResult(StepExecutionStatus.ERROR, "failed the task")));

		Assertions.assertEquals(1, _collector.getData().size());
	    Assertions.assertFalse(_collector.getResult().isPass());
	    Assertions.assertEquals(2, _collector.getResult().getFailures().size());
	    }

	@Test
    void honorFailOnErrorSetting() throws MuseExecutionError
		{
	    _collector = createCollector(false, null, null);
	    _collector.initialize(_context);
	    _context.raiseEvent(EndStepEventType.create(_step, new MockStepExecutionContext(), new BasicStepExecutionResult(StepExecutionStatus.ERROR, "failed the task")));

		Assertions.assertEquals(1, _collector.getData().size());
	    Assertions.assertTrue(_collector.getResult().isPass());
	    Assertions.assertEquals(0, _collector.getResult().getFailures().size());
	    }

	@Test
    void honorFailOnFailureSetting() throws MuseExecutionError
		{
	    _collector = createCollector(null, false, null);
	    _collector.initialize(_context);
		_context.raiseEvent(EndStepEventType.create(_step, new MockStepExecutionContext(), new BasicStepExecutionResult(StepExecutionStatus.FAILURE, "failed the task")));

		Assertions.assertEquals(1, _collector.getData().size());
	    Assertions.assertTrue(_collector.getResult().isPass());
	    Assertions.assertEquals(0, _collector.getResult().getFailures().size());
	    }

	@Test
    void honorFailOnInterruptSetting() throws MuseExecutionError
		{
	    _collector = createCollector(null, null, false);
	    _collector.initialize(_context);
		_context.raiseEvent(new MuseEvent(new InterruptedEventType()));

		Assertions.assertEquals(1, _collector.getData().size());
	    Assertions.assertTrue(_collector.getResult().isPass());
	    Assertions.assertEquals(0, _collector.getResult().getFailures().size());
	    }

	@Test
    void captureStorageLocation() throws MuseExecutionError
		{
	    _collector = createCollector(null, null, false);
	    _collector.initialize(_context);
		_context.raiseEvent(LocalStorageLocationEventType.create("path/to/files", null));

	    Assertions.assertEquals("path/to/files", _collector.getResult().getStorageLocation());
	    }

	private TaskResultCollector createCollector(Boolean fail_on_error, Boolean fail_on_failure, Boolean fail_on_interrupt)
		{
		final TaskResultCollectorConfiguration configuration = new TaskResultCollectorConfiguration();
		if (fail_on_error != null)
			configuration.parameters().addSource(TaskResultCollectorConfiguration.FAIL_ON_ERROR, ValueSourceConfiguration.forValue(fail_on_error));
		if (fail_on_failure != null)
			configuration.parameters().addSource(TaskResultCollectorConfiguration.FAIL_ON_FAILURE, ValueSourceConfiguration.forValue(fail_on_failure));
		if (fail_on_interrupt != null)
			configuration.parameters().addSource(TaskResultCollectorConfiguration.FAIL_ON_INTERRUPT, ValueSourceConfiguration.forValue(fail_on_interrupt));
		return new TaskResultCollector(configuration);
		}

	@BeforeEach
    void setup() throws MuseExecutionError
		{
		_collector = createCollector(null, null, null);
		_context = new MockMuseExecutionContext();
	    _collector.initialize(_context);
		_step = new StepConfiguration("step-type");
		_step.setStepId(99L);
		_task = new SteppedTask(_step);
		_task.setId("task1");
		}

	private TaskResultCollector _collector;
	private MockMuseExecutionContext _context;
	private StepConfiguration _step;
	private SteppedTask _task;
	}
