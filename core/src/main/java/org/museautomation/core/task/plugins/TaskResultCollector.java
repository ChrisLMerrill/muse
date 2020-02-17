package org.museautomation.core.task.plugins;

import org.museautomation.core.*;
import org.museautomation.core.datacollection.*;
import org.museautomation.core.events.*;
import org.museautomation.core.execution.*;
import org.museautomation.core.plugins.*;
import org.museautomation.core.resource.generic.*;
import org.museautomation.core.values.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class TaskResultCollector extends GenericConfigurableTaskPlugin implements DataCollector
	{
	public TaskResultCollector(GenericResourceConfiguration configuration)
		{
		super(configuration);
		}

	@Override
	public List<TaskResultData> getData()
		{
		return Collections.singletonList(_result);
		}

	public TaskResult getResult()
		{
		return _result;
		}

	@Override
	public void initialize(MuseExecutionContext context) throws MuseExecutionError
		{
		Boolean fail_on_error = BaseValueSource.getValue(BaseValueSource.getValueSource(_configuration.parameters(), TaskResultCollectorConfiguration.FAIL_ON_ERROR, false, context.getProject()), context, true, Boolean.class);
		if (fail_on_error != null)
			_fail_on_error = fail_on_error;
		Boolean fail_on_failure = BaseValueSource.getValue(BaseValueSource.getValueSource(_configuration.parameters(), TaskResultCollectorConfiguration.FAIL_ON_FAILURE, false, context.getProject()), context, true, Boolean.class);
		if (fail_on_failure != null)
			_fail_on_failure = fail_on_failure;
		Boolean fail_on_interrupt = BaseValueSource.getValue(BaseValueSource.getValueSource(_configuration.parameters(), TaskResultCollectorConfiguration.FAIL_ON_INTERRUPT, false, context.getProject()), context, true, Boolean.class);
		if (fail_on_interrupt != null)
			_fail_on_interrupt = fail_on_interrupt;

		context.addEventListener(new MuseEventListener()
			{
			@Override
			public void eventRaised(MuseEvent event)
				{
				if (event.getTypeId().equals(StartTaskEventType.TYPE_ID))
					{
					_result = new TaskResult();
					_result.setTaskId(event.getAttributeAsString(StartTaskEventType.TASK_ID));
					_result.setName(event.getAttributeAsString(StartTaskEventType.TASK_NAME));
					}
				else if (event.getTypeId().equals(EndTaskEventType.TYPE_ID))
					{
					context.removeEventListener(this);
					String summary = "Test completed successfully";
					if (_result.getFailures().size() > 0)
						summary = String.format("Test failed with %d failure(s) and %d error(s).", countFailures(_result, TaskResult.FailureType.Failure), countFailures(_result, TaskResult.FailureType.Error));
					_result.setSummary(summary);
					}
				else if (event.getTypeId().equals(InterruptedEventType.TYPE_ID) && _fail_on_interrupt)
                    {
                    _result.addFailure(new TaskResult.Failure(TaskResult.FailureType.Interrupted, event.getAttributeAsString(MuseEvent.DESCRIPTION)));
                    _result.setPass(false);
                    }
                else
					{
					if (event.hasTag(MuseEvent.FAILURE) && _fail_on_failure)
						{
						_result.addFailure(new TaskResult.Failure(TaskResult.FailureType.Failure, event.getAttributeAsString(MuseEvent.DESCRIPTION)));
						_result.setPass(false);
						}
					else if (event.hasTag(MuseEvent.ERROR) && _fail_on_error)
						{
						_result.addFailure(new TaskResult.Failure(TaskResult.FailureType.Error, event.getAttributeAsString(MuseEvent.DESCRIPTION)));
						_result.setPass(false);
						}
					}
				}
			});
		}

	private int countFailures(TaskResult result, TaskResult.FailureType type)
		{
		int count = 0;
		for (TaskResult.Failure failure : result.getFailures())
			if (failure.getType().equals(type))
				count++;
		return count;
		}

	private boolean _fail_on_failure = true;
	private boolean _fail_on_error = true;
	private boolean _fail_on_interrupt = true;

	private TaskResult _result = new TaskResult();
	}
