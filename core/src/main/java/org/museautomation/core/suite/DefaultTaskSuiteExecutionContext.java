package org.museautomation.core.suite;

import org.museautomation.core.*;
import org.museautomation.core.context.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class DefaultTaskSuiteExecutionContext extends BaseExecutionContext implements TaskSuiteExecutionContext
	{
	public DefaultTaskSuiteExecutionContext(MuseProject project, MuseTaskSuite suite)
		{
		super(project, ContextVariableScope.Suite);
		_suite = suite;
		}

	public DefaultTaskSuiteExecutionContext(MuseExecutionContext parent, MuseTaskSuite suite)
		{
		super(parent, ContextVariableScope.Suite);
		_suite = suite;
		}

	@Override
	public MuseTaskSuite getSuite()
		{
		return _suite;
		}

	@Override
	public synchronized String getTaskExecutionId(DefaultTaskExecutionContext task_context)
		{
		int hash = task_context.hashCode();  // we use the hash to ensure references to obsolete contexts are not retained
		String execution_id = _task_execution_ids.get(hash);
		if (execution_id != null)
			return execution_id;

		final String task_id = task_context.getTask().getId();
		Integer suffix = _next_task_suffix.get(task_id);
		if (suffix == null)
			suffix = 1;

		execution_id = task_id + "-" + suffix;
		_next_task_suffix.put(task_id, ++suffix);
		_task_execution_ids.put(hash, execution_id);

		return execution_id;
		}

	private Map<Integer, String> _task_execution_ids = new HashMap<>();
	private Map<String, Integer> _next_task_suffix = new HashMap();

	private MuseTaskSuite _suite;
	}
