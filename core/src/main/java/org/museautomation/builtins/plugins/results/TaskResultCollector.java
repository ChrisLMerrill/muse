package org.museautomation.builtins.plugins.results;

import org.museautomation.builtins.plugins.resultstorage.*;
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
		return Collections.singletonList(getResult());
		}

	public TaskResult getResult()
		{
        _result.setSummary(_summary_failure_message);
		if (_result.getSummary() == null)
            _result.setSummary("Task completed successfully");
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

        LocalStorageLocationPlugin storage = Plugins.findType(LocalStorageLocationPlugin.class, context);
        if (storage != null)
            _result.setStorageLocation(storage.getBaseFolder().getAbsolutePath());

		context.addEventListener(event ->
            {
            EventType type = EventTypes.get(context.getProject()).findType(event.getTypeId());
            String description = type.getDescription(event);

            if (event.getTypeId().equals(StartTaskEventType.TYPE_ID))
                {
                _result.setTaskId(event.getAttributeAsString(StartTaskEventType.TASK_ID));
                _result.setName(event.getAttributeAsString(StartTaskEventType.TASK_NAME));
                }
            else if (event.getTypeId().equals(LocalStorageLocationEventType.TYPE_ID))
                {
                _result.setStorageLocation(new LocalStorageLocationEventType().getBasePath(event));
                }
            else if (event.getTypeId().equals(InterruptedEventType.TYPE_ID) && _fail_on_interrupt)
                {
                updateSummary(TaskResult.FailureType.Failure, description, type);
                _result.addFailure(new TaskResult.Failure(TaskResult.FailureType.Interrupted, description));
                _result.setPass(false);
                }
            else if (event.hasTag(MuseEvent.FAILURE) && _fail_on_failure)
                {
                updateSummary(TaskResult.FailureType.Failure, description, type);
                _result.addFailure(new TaskResult.Failure(TaskResult.FailureType.Failure, description));
                _result.setPass(false);
                }
            else if (event.hasTag(MuseEvent.ERROR) && _fail_on_error)
                {
                updateSummary(TaskResult.FailureType.Error, description, type);
                _result.addFailure(new TaskResult.Failure(TaskResult.FailureType.Error, description));
                _result.setPass(false);
                }
            });
		}

    private void updateSummary(TaskResult.FailureType type, String description, EventType event_type)
        {
        if (_summary_failure_type == null ||
            (type == TaskResult.FailureType.Failure && _summary_failure_type == TaskResult.FailureType.Error))
            {
            _summary_failure_type = type;
            if (description == null)
                _summary_failure_message = event_type.getName();
            else
                _summary_failure_message = description.split("\n", 2)[0];  // only first line of the message
            }
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
	private TaskResult.FailureType _summary_failure_type = null;
	private String _summary_failure_message = null;

	private final TaskResult _result = new TaskResult();
	}
