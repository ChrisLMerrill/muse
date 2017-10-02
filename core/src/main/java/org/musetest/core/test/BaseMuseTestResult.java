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
        _failure = failure;
        }

    public BaseMuseTestResult(MuseTest test, MuseExecutionContext context, MuseTestFailureDescription failure)
        {
        _test = test;
        for (DataCollector collector : context.getDataCollectors())
        	_result_data.add(collector.getData());
        _failure = failure;
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
        return _failure == null;
        }

    @Override
    public MuseTestFailureDescription getFailureDescription()
        {
        return _failure;
        }

    @Override
    public String getOneLineDescription()
        {
        if (isPass())
            return String.format("SUCCESS: Test '%s' completed successfully.", _test.getDescription());
        else
            {
            String start = String.format(FAILURE_STRINGS.get(_failure.getFailureType()), _failure.getFailureType().name().toUpperCase(), _test.getDescription());
            String reason = _failure.getReason();
            if (reason == null)
                reason = UNDESCRIBED;
            return start + " Reason is: " + reason;
            }
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
    private MuseTestFailureDescription _failure;
    private TestConfiguration _config;
    private List<TestResultData> _result_data = new ArrayList<>();

    private static Map<MuseTestFailureDescription.FailureType, String> FAILURE_STRINGS = new HashMap<>();
    static
        {
        FAILURE_STRINGS.put(MuseTestFailureDescription.FailureType.Error, "%s: Test '%s' was not completed due to an error.");
        FAILURE_STRINGS.put(MuseTestFailureDescription.FailureType.Failure, "%s: Test '%s' failed.");
        FAILURE_STRINGS.put(MuseTestFailureDescription.FailureType.Interrupted, "%s: Test '%s' was not completed due to an error.");
        }
    private final static String UNDESCRIBED = "(no description provided)";
    }


