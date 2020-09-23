package org.museautomation.core.context;

import org.museautomation.core.*;
import org.museautomation.core.output.*;
import org.museautomation.core.suite.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class DefaultTaskExecutionContext extends BaseExecutionContext implements TaskExecutionContext
	{
	public DefaultTaskExecutionContext(MuseProject project, MuseTask task)
		{
		this(new ProjectExecutionContext(project), task);
		}

	public DefaultTaskExecutionContext(MuseExecutionContext parent_context, MuseTask task)
		{
		super(parent_context, ContextVariableScope.Execution);
		_task = task;
		_outputs = new ExecutionOutputs();
		}

	@Override
	public MuseTask getTask()
		{
		return _task;
		}

	@Override
	public String getTaskExecutionId()
		{
		if (_task_id == null)
			{
			TaskSuiteExecutionContext suite_context = MuseExecutionContext.findAncestor(this, TaskSuiteExecutionContext.class);
			if (suite_context != null)
				_task_id = suite_context.getTaskExecutionId(this);
			else
				_task_id = getTask().getId() + "-" + System.currentTimeMillis();
			}
		return _task_id;
		}

    protected final MuseTask _task;
	private String _task_id;
	}
