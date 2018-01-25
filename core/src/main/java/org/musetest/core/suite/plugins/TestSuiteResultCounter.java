package org.musetest.core.suite.plugins;

import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.datacollection.*;
import org.musetest.core.events.*;
import org.musetest.core.suite.plugin.*;
import org.musetest.core.test.plugin.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class TestSuiteResultCounter extends BaseTestPlugin implements TestSuitePlugin, DataCollector
	{
	TestSuiteResultCounter(TestSuiteResultCounterConfiguration config)
		{
		super(config);
		}

	@Override
	public boolean shouldAddToSuite(MuseExecutionContext context, MuseTestSuite suite, boolean automatic) throws MuseExecutionError
		{
		return super.shouldAddToTestContext(context, automatic);
		}

	@Override
	public boolean shouldAddToTestContext(MuseExecutionContext context, boolean automatic)
		{
		return false;
		}

	@Override
	public TestSuiteResultCounts getData()
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