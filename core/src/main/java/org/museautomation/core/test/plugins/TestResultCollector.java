package org.museautomation.core.test.plugins;

import org.museautomation.core.*;
import org.museautomation.core.datacollection.*;
import org.museautomation.core.events.*;
import org.museautomation.core.execution.*;
import org.museautomation.core.plugins.*;
import org.museautomation.core.resource.generic.*;
import org.museautomation.core.values.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class TestResultCollector extends GenericConfigurableTestPlugin implements DataCollector
	{
	public TestResultCollector(GenericResourceConfiguration configuration)
		{
		super(configuration);
		}

	@Override
	public List<TestResultData> getData()
		{
		return Collections.singletonList(_result);
		}

	public TestResult getResult()
		{
		return _result;
		}

	@Override
	public void initialize(MuseExecutionContext context) throws MuseExecutionError
		{
		Boolean fail_on_error = BaseValueSource.getValue(BaseValueSource.getValueSource(_configuration.parameters(), TestResultCollectorConfiguration.FAIL_ON_ERROR, false, context.getProject()), context, true, Boolean.class);
		if (fail_on_error != null)
			_fail_on_error = fail_on_error;
		Boolean fail_on_failure = BaseValueSource.getValue(BaseValueSource.getValueSource(_configuration.parameters(), TestResultCollectorConfiguration.FAIL_ON_FAILURE, false, context.getProject()), context, true, Boolean.class);
		if (fail_on_failure != null)
			_fail_on_failure = fail_on_failure;
		Boolean fail_on_interrupt = BaseValueSource.getValue(BaseValueSource.getValueSource(_configuration.parameters(), TestResultCollectorConfiguration.FAIL_ON_INTERRUPT, false, context.getProject()), context, true, Boolean.class);
		if (fail_on_interrupt != null)
			_fail_on_interrupt = fail_on_interrupt;

		context.addEventListener(new MuseEventListener()
			{
			@Override
			public void eventRaised(MuseEvent event)
				{
				if (event.getTypeId().equals(StartTestEventType.TYPE_ID))
					{
					_result = new TestResult();
					_result.setTestId(event.getAttributeAsString(StartTestEventType.TEST_ID));
					_result.setName(event.getAttributeAsString(StartTestEventType.TEST_NAME));
					}
				else if (event.getTypeId().equals(EndTestEventType.TYPE_ID))
					{
					context.removeEventListener(this);
					String summary = "Test completed successfully";
					if (_result.getFailures().size() > 0)
						summary = String.format("Test failed with %d failure(s) and %d error(s).", countFailures(_result, TestResult.FailureType.Failure), countFailures(_result, TestResult.FailureType.Error));
					_result.setSummary(summary);
					}
				else if (event.getTypeId().equals(InterruptedEventType.TYPE_ID) && _fail_on_interrupt)
                    {
                    _result.addFailure(new TestResult.Failure(TestResult.FailureType.Interrupted, event.getAttributeAsString(MuseEvent.DESCRIPTION)));
                    _result.setPass(false);
                    }
                else
					{
					if (event.hasTag(MuseEvent.FAILURE) && _fail_on_failure)
						{
						_result.addFailure(new TestResult.Failure(TestResult.FailureType.Failure, event.getAttributeAsString(MuseEvent.DESCRIPTION)));
						_result.setPass(false);
						}
					else if (event.hasTag(MuseEvent.ERROR) && _fail_on_error)
						{
						_result.addFailure(new TestResult.Failure(TestResult.FailureType.Error, event.getAttributeAsString(MuseEvent.DESCRIPTION)));
						_result.setPass(false);
						}
					}
				}
			});
		}

	private int countFailures(TestResult result, TestResult.FailureType type)
		{
		int count = 0;
		for (TestResult.Failure failure : result.getFailures())
			if (failure.getType().equals(type))
				count++;
		return count;
		}

	private boolean _fail_on_failure = true;
	private boolean _fail_on_error = true;
	private boolean _fail_on_interrupt = true;

	private TestResult _result = new TestResult();
	}
