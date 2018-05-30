package org.musetest.core.suite.plugins;

import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.datacollection.*;
import org.musetest.core.events.*;
import org.musetest.core.plugins.*;
import org.musetest.core.suite.*;

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
		return context instanceof TestSuiteExecutionContext || context instanceof TestExecutionContext;
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
		if (!(context instanceof TestExecutionContext))
			return;
		context.addEventListener(new EventListener((TestExecutionContext) context));
		}

	class EventListener implements MuseEventListener
		{
		EventListener(TestExecutionContext context)
			{
			_context = context;
			}

		@Override
		public void eventRaised(MuseEvent event)
			{
			if (EndTestEventType.TYPE_ID.equals(event.getTypeId()))
				{
				TestResult result = TestResult.find(_context);
				if (result != null)
					{
					if (result.hasErrors())
						_counts._errors++;
					else if (result.hasFailures())
						_counts._failures++;
					else
						_counts._successes++;
					}
				_context.removeEventListener(this);
				}
			}

		private TestExecutionContext _context;
		}

	private TestSuiteResultCounts _counts = new TestSuiteResultCounts();
	}