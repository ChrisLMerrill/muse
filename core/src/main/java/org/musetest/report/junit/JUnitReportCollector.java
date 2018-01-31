package org.musetest.report.junit;

import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.datacollection.*;
import org.musetest.core.events.*;
import org.musetest.core.plugins.*;
import org.musetest.core.suite.*;

import javax.annotation.*;

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
		return context instanceof TestSuiteExecutionContext;
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
