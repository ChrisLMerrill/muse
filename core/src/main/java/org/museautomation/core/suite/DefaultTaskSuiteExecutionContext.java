package org.museautomation.core.suite;

import org.museautomation.core.*;
import org.museautomation.core.context.*;
import org.museautomation.core.events.*;
import org.museautomation.core.task.*;
import org.slf4j.*;

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

		Object val = task_context.getVariable(StartSuiteTaskEventType.CONFIG_VAR_NAME);
		if (!(val instanceof TaskConfiguration))
            {
            LOG.error(String.format("The value of the task configuration variable (%s) is not a TaskConfiguration. It is a %s, which is not allowed.", StartSuiteTaskEventType.CONFIG_VAR_NAME, val.getClass().getSimpleName()));
            execution_id = task_context.getTask().getId() + "-" + System.currentTimeMillis();
            }
        else
            execution_id = ((TaskConfiguration)val).name();

		_task_execution_ids.put(hash, execution_id);
		return execution_id;
		}

	private final Map<Integer, String> _task_execution_ids = new HashMap<>();
	private final MuseTaskSuite _suite;

	final static Logger LOG = LoggerFactory.getLogger(DefaultTaskSuiteExecutionContext.class);
	}