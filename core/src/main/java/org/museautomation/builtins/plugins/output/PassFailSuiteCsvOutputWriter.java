package org.museautomation.builtins.plugins.output;

import org.museautomation.core.*;
import org.museautomation.core.context.*;
import org.museautomation.core.datacollection.*;
import org.museautomation.core.events.*;
import org.museautomation.core.plugins.*;
import org.museautomation.core.suite.*;
import org.museautomation.core.task.*;
import org.museautomation.core.values.*;
import org.slf4j.*;

import javax.annotation.*;
import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class PassFailSuiteCsvOutputWriter extends GenericConfigurablePlugin implements DataCollector
	{
	PassFailSuiteCsvOutputWriter(PassFailSuiteCsvOutputWriterConfiguration configuration)
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
        try
            {
            MuseValueSource source = BaseValueSource.getValueSource(_configuration.parameters(), PassFailSuiteCsvOutputWriterConfiguration.VARIABLE_NAMES_LIST_PARAM, true, context.getProject());
            List<String> list = (List<String>) BaseValueSource.getValue(source, context, false, List.class);
            _names = list.toArray(String[]::new);
            _data.setColumnNames(_names);

            source = BaseValueSource.getValueSource(_configuration.parameters(), PassFailSuiteCsvOutputWriterConfiguration.FILENAME_PARAM, false, context.getProject());
            if (source != null)
                {
                String filename = BaseValueSource.getValue(source, context, true, String.class);
                if (filename != null)
                    _data.setName(filename);
                }

            source = BaseValueSource.getValueSource(_configuration.parameters(), PassFailSuiteCsvOutputWriterConfiguration.SUCCESSFUL_TASKS_PARAM, false, context.getProject());
            if (source != null)
                _collect_on_success = BaseValueSource.getValue(source, context, false, Boolean.class);

            source = BaseValueSource.getValueSource(_configuration.parameters(), PassFailSuiteCsvOutputWriterConfiguration.FAILED_TASKS_PARAM, false, context.getProject());
            if (source != null)
                _collect_on_failure = BaseValueSource.getValue(source, context, false, Boolean.class);
            }
        catch (Exception e)
            {
            LOG.error("Unable to initialize PassFailSuiteCsvOutputWriter plugin.", e);
            }

        if (context instanceof TaskSuiteExecutionContext)
            {
            context.addEventListener(new EventListener((TaskSuiteExecutionContext)context));
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
                if (result == null)
                    LOG.error("No task result found. Unable to extract values.");
                else if (result.isPass() && _collect_on_success || result.hasFailures() && _collect_on_failure)
                    {
                    String[] row = new String[_names.length];
                    for (int i = 0; i < _names.length; i++)
                        {
                        Object variable = context.getVariable(_names[i]);
                        if (variable != null)
                            row[i] = variable.toString();
                        }
                    _data.addRow(row);
                    }
                }
			}

		private final TaskSuiteExecutionContext _context;
		}

	private final CsvResultData _data = new CsvResultData();
	private String[] _names;
	private boolean _collect_on_success = false;
	private boolean _collect_on_failure = false;

    private final static Logger LOG = LoggerFactory.getLogger(PassFailSuiteCsvOutputWriter.class);
	}
