package org.musetest.report.junit;

import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.datacollection.*;
import org.musetest.core.events.*;
import org.musetest.core.suite.plugin.*;
import org.musetest.core.test.plugin.*;

import javax.annotation.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class JUnitReportCollector extends BaseTestPlugin implements TestSuitePlugin, DataCollector
	{
	JUnitReportCollector(JUnitReportCollectorConfiguration configuration)
		{
		super(configuration);
		}

	@Override
	public boolean shouldAddToSuite(MuseExecutionContext context, MuseTestSuite suite, boolean automatic) throws MuseExecutionError
		{
		final boolean add = super.shouldAddToTestContext(context, automatic);
		if (add)
			_data.setSuiteName(suite.getId());
		return add;
		}

	@Override
	public boolean shouldAddToTestContext(MuseExecutionContext context, boolean automatic)
		{
		return true;
		}

	@Nullable
	@Override
	public TestResultData getData()
		{
		return _data;
		}

	@Override
	public void initialize(MuseExecutionContext context)
		{
System.out.println("JUnitReportCollector plugin has been initialized");
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
					_data.addResult(result, _context.getDataCollector(EventLogger.class).getData());
				_context.removeEventListener(this);
				}
			}

		private TestExecutionContext _context;
		}

	private JUnitReportData _data = new JUnitReportData();
	}
