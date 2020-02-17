package org.museautomation.builtins.plugins.suite;

import org.museautomation.core.*;
import org.museautomation.core.context.*;
import org.museautomation.core.datacollection.*;
import org.museautomation.core.events.*;
import org.museautomation.core.plugins.*;
import org.museautomation.core.suite.*;
import org.museautomation.core.task.*;
import org.slf4j.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class TaskSuiteResultCounter extends GenericConfigurablePlugin implements DataCollector
	{
	TaskSuiteResultCounter(TaskSuiteResultCounterConfiguration config)
		{
		super(config);
		}

	@Override
	protected boolean applyToContextType(MuseExecutionContext context)
		{
		return context instanceof TaskSuiteExecutionContext;
		}

	@Override
	public List<TaskResultData> getData()
		{
		return Collections.singletonList(_counts);
		}

	public TaskSuiteResultCounts getResult()
		{
		return _counts;
		}

	@Override
	public void initialize(MuseExecutionContext context)
		{
		if (context instanceof TaskSuiteExecutionContext)
			context.addEventListener(new EventListener((TaskSuiteExecutionContext)context));
		}

	class EventListener implements MuseEventListener
		{
		EventListener(TaskSuiteExecutionContext context)
			{
			_context = context;
			}

		@Override
		public void eventRaised(MuseEvent event)
			{
			if (EndSuiteTaskEventType.TYPE_ID.equals(event.getTypeId()))
				{
				final String varname = EndSuiteTaskEventType.getConfigVariableName(event);
				Object task_config = _context.getVariable(varname);
				if (task_config == null || !(task_config instanceof TaskConfiguration))
					{
					String message;
					if (task_config == null)
						message = String.format("The task config was not found: variable %s was null", varname);
					else
						message = String.format("The task config variable holds a %s. Was expecting a TaskConfiguration", task_config.getClass().getSimpleName());
					LOG.error(message);
					_context.raiseEvent(MessageEventType.create(message));
					return;
					}
				TaskExecutionContext context = ((TaskConfiguration)task_config).context();
				if (context == null)
					{
					String message = "The context is null";
					LOG.error(message);
					_context.raiseEvent(MessageEventType.create(message));
					return;
					}
				TaskResult result = TaskResult.find(context);
				if (result != null)
					{
					if (result.hasErrors())
						_counts._errors++;
					else if (result.hasFailures())
						_counts._failures++;
					else
						_counts._successes++;
					}
				}
			}

		private TaskSuiteExecutionContext _context;
		}

	private TaskSuiteResultCounts _counts = new TaskSuiteResultCounts();

	private final static Logger LOG = LoggerFactory.getLogger(TaskSuiteResultCounter.class);
	}