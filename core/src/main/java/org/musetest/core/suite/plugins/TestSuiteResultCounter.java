package org.musetest.core.suite.plugins;

import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.datacollection.*;
import org.musetest.core.events.*;
import org.musetest.core.plugins.*;
import org.musetest.core.suite.*;
import org.musetest.core.test.*;
import org.slf4j.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class TestSuiteResultCounter extends GenericConfigurablePlugin implements DataCollector
	{
	TestSuiteResultCounter(TestSuiteResultCounterConfiguration config)
		{
		super(config);
		}

	@Override
	protected boolean applyToContextType(MuseExecutionContext context)
		{
		return context instanceof TestSuiteExecutionContext;
		}

	@Override
	public List<TestResultData> getData()
		{
		return Collections.singletonList(_counts);
		}

	public TestSuiteResultCounts getResult()
		{
		return _counts;
		}

	@Override
	public void initialize(MuseExecutionContext context)
		{
		if (context instanceof TestSuiteExecutionContext)
			context.addEventListener(new EventListener((TestSuiteExecutionContext)context));
		}

	class EventListener implements MuseEventListener
		{
		EventListener(TestSuiteExecutionContext context)
			{
			_context = context;
			}

		@Override
		public void eventRaised(MuseEvent event)
			{
			if (EndSuiteTestEventType.TYPE_ID.equals(event.getTypeId()))
				{
				final String varname = EndSuiteTestEventType.getConfigVariableName(event);
				Object test_config = _context.getVariable(varname);
				if (test_config == null || !(test_config instanceof TestConfiguration))
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
				TestExecutionContext context = ((TestConfiguration)test_config).context();
				if (context == null)
					{
					String message = "The test context is null";
					LOG.error(message);
					_context.raiseEvent(MessageEventType.create(message));
					return;
					}
				TestResult result = TestResult.find(context);
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

		private TestSuiteExecutionContext _context;
		}

	private TestSuiteResultCounts _counts = new TestSuiteResultCounts();

	private final static Logger LOG = LoggerFactory.getLogger(TestSuiteResultCounter.class);
	}