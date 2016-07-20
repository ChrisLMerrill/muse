package org.musetest.core.test;

import org.musetest.core.*;
import org.musetest.core.events.*;
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

    @Override
    public MuseTest getTest()
        {
        return _test;
        }

    @Override
    public EventLog getLog()
        {
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

    private MuseTest _test;
    private EventLog _log;
    private MuseTestFailureDescription _failure;

    private static Map<MuseTestFailureDescription.FailureType, String> FAILURE_STRINGS = new HashMap<>();
    static
        {
        FAILURE_STRINGS.put(MuseTestFailureDescription.FailureType.Error, "%s: Test '%s' was not completed due to an error.");
        FAILURE_STRINGS.put(MuseTestFailureDescription.FailureType.Failure, "%s: Test '%s' failed.");
        FAILURE_STRINGS.put(MuseTestFailureDescription.FailureType.Interrupted, "%s: Test '%s' was not completed due to an error.");
        }
    private final static String UNDESCRIBED = "(no description provided)";
    }


