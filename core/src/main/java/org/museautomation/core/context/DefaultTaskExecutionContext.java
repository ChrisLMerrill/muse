package org.museautomation.core.context;

import org.museautomation.core.*;
import org.museautomation.core.suite.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class DefaultTaskExecutionContext extends BaseExecutionContext implements TaskExecutionContext
	{
	public DefaultTaskExecutionContext(MuseProject project, MuseTask test)
		{
		this(new ProjectExecutionContext(project), test);
		}

	public DefaultTaskExecutionContext(MuseExecutionContext parent_context, MuseTask test)
		{
		super(parent_context, ContextVariableScope.Execution);
		_task = test;
		}

	@Override
	public MuseTask getTask()
		{
		return _task;
		}

	@Override
	public String getTaskExecutionId()
		{
		if (_test_id == null)
			{
			TaskSuiteExecutionContext suite_context = MuseExecutionContext.findAncestor(this, TaskSuiteExecutionContext.class);
			if (suite_context != null)
				_test_id = suite_context.getTaskExecutionId(this);
			else
				_test_id = Long.toString(System.currentTimeMillis());
			}
		return _test_id;
		}

	protected final MuseTask _task;
	private String _test_id;
	}
