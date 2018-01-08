package org.musetest.core.test;

import org.musetest.core.*;
import org.musetest.core.datacollection.*;
import org.musetest.core.events.*;
import org.musetest.core.suite.*;
import org.musetest.core.variables.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class BaseMuseTestResult implements MuseTestResult
    {
    public BaseMuseTestResult(MuseTest test, EventLog log, MuseTestFailureDescription failure)
        {
        _test = test;
        _log = log;
        if (failure != null)
	        _failures.add(failure);
        }

    public BaseMuseTestResult(MuseTest test, MuseExecutionContext context, MuseTestFailureDescription failure)
        {
        this(test, context, Collections.singletonList(failure));
        }

    public BaseMuseTestResult(MuseTest test, MuseExecutionContext context, List<MuseTestFailureDescription> failures)
        {
        _test = test;
        for (DataCollector collector : context.getDataCollectors())
	        {
	        final TestResultData collected = collector.getData();
	        if (collected != null)
		        _result_data.add(collected);
	        }
        if (failures != null)
            _failures.addAll(failures);
        }

    @Override
    public MuseTest getTest()
        {
        return _test;
        }

    @Override
    public EventLog getLog()
        {
        for (TestResultData data : _result_data)
        	if (data instanceof EventLog)
        		return (EventLog) data;
        return _log;
        }

    public void setLog(EventLog log)
        {
        _log = log;
        }

    @Override
    public boolean isPass()
        {
        return _failures.size() == 0;
        }

    @Override
    public MuseTestFailureDescription getFailureDescription()
        {
        if (_failures.isEmpty())
        	return null;
        return _failures.get(0);
        }

    @Override
    public String getOneLineDescription()
        {
        if (isPass())
            return String.format("SUCCESS: Test '%s' completed successfully.", _test.getDescription());
        else
            {
            MuseTestFailureDescription first_failure = _failures.get(0);
            String reason = first_failure.getReason();
            MuseTestFailureDescription.FailureType severity = getSeverity();
            if (reason == null)
                reason = UNDESCRIBED;
            if (_failures.size() == 1)
	            return String.format(SINGLE_FAILURE_STRINGS.get(severity), severity.name().toUpperCase(), _test.getDescription(), reason);
            else
	            return String.format(MULTIPLE_FAILURE_STRINGS.get(severity), severity.name().toUpperCase(), _test.getDescription(), _failures.size(), reason);
            }
        }

    private MuseTestFailureDescription.FailureType getSeverity()
	    {
	    MuseTestFailureDescription.FailureType severity = null;
	    for (MuseTestFailureDescription failure : _failures)
		    {
		    final MuseTestFailureDescription.FailureType type = failure.getFailureType();
		    if (type.equals(MuseTestFailureDescription.FailureType.Failure) && severity == null)
			    severity = MuseTestFailureDescription.FailureType.Failure;
		    else if (type.equals(MuseTestFailureDescription.FailureType.Interrupted) && (severity != MuseTestFailureDescription.FailureType.Error))
		    	severity = MuseTestFailureDescription.FailureType.Interrupted;
		    else if (type.equals(MuseTestFailureDescription.FailureType.Error))
		    	severity = MuseTestFailureDescription.FailureType.Error;
		    }
	    return severity;
	    }

    @Override
    public String getName()
        {
        if (_config == null)
            return _test.getDescription();
        return _config.getName();
        }

    @Override
    public TestConfiguration getConfiguration()
        {
        return _config;
        }

    public void setConfiguration(TestConfiguration config)
        {
        _config = config;
        }

    private MuseTest _test;
    private EventLog _log;
    private List<MuseTestFailureDescription> _failures = new ArrayList<>();
    private TestConfiguration _config;
    private List<TestResultData> _result_data = new ArrayList<>();

    private static Map<MuseTestFailureDescription.FailureType, String> SINGLE_FAILURE_STRINGS = new HashMap<>();
    static
        {
        SINGLE_FAILURE_STRINGS.put(MuseTestFailureDescription.FailureType.Error, "%s: Test '%s' was not completed due to an error. Reason is: %s");
        SINGLE_FAILURE_STRINGS.put(MuseTestFailureDescription.FailureType.Failure, "%s: Test '%s' failed. Reason is: %s");
        SINGLE_FAILURE_STRINGS.put(MuseTestFailureDescription.FailureType.Interrupted, "%s: Test '%s' was not completed due to an error. Reason is: %s");
        }
    private static Map<MuseTestFailureDescription.FailureType, String> MULTIPLE_FAILURE_STRINGS = new HashMap<>();
    static
        {
        MULTIPLE_FAILURE_STRINGS.put(MuseTestFailureDescription.FailureType.Error, "%s: Test '%s' was not completed due to %d failures and/or errors. First is: %s");
        MULTIPLE_FAILURE_STRINGS.put(MuseTestFailureDescription.FailureType.Failure, "%s: Test '%s' encountered %d failures. First is: %s");
        MULTIPLE_FAILURE_STRINGS.put(MuseTestFailureDescription.FailureType.Interrupted, "%s: Test '%s' was not completed due to %d errors. First is: %s");
        }
    private final static String UNDESCRIBED = "(no description provided)";
    }


