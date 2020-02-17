package org.museautomation.junit;

import org.museautomation.core.*;
import org.museautomation.core.context.*;
import org.museautomation.core.datacollection.*;
import org.museautomation.core.events.*;
import org.museautomation.core.plugins.*;
import org.museautomation.core.suite.*;
import org.museautomation.core.task.*;
import org.slf4j.*;

import javax.annotation.*;
import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class JUnitReportCollector extends GenericConfigurablePlugin implements DataCollector
	{
	JUnitReportCollector(JUnitReportCollectorConfiguration configuration)
		{
		super(configuration);
		}

	@Override
	protected boolean applyToContextType(MuseExecutionContext context)
		{
		return context instanceof TaskSuiteExecutionContext;
		}

	@Nullable
	@Override
	public List<TaskResultData> getData()
		{
		return Collections.singletonList(_data);
		}

	@Override
	public void initialize(MuseExecutionContext context)
		{
        if (context instanceof TaskSuiteExecutionContext)
            {
            context.addEventListener(new EventListener((TaskSuiteExecutionContext)context));
            _data.setSuiteName(((TaskSuiteExecutionContext) context).getSuite().getId());
            }
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
                Object test_config = _context.getVariable(varname);
                if (!(test_config instanceof TaskConfiguration))
                    {
                    String message;
                    if (test_config == null)
                        message = String.format("The test config was not found: variable %s was null", varname);
                    else
                        message = String.format("The test config variable holds a %s. Was expecting a TestConfig", test_config.getClass().getSimpleName());
                    LOG.error(message);
                    _context.raiseEvent(MessageEventType.create(message));
                    return;
                    }
                TaskExecutionContext context = ((TaskConfiguration)test_config).context();
                if (context == null)
                    {
                    String message = "The test context is null";
                    LOG.error(message);
                    _context.raiseEvent(MessageEventType.create(message));
                    return;
                    }
                TaskResult result = TaskResult.find(context);
                if (result != null)
                    _data.addResult(result, context.getEventLog());
                }
			}

		private TaskSuiteExecutionContext _context;
		}

	private JUnitReportData _data = new JUnitReportData();

    private final static Logger LOG = LoggerFactory.getLogger(JUnitReportCollector.class);
	}
