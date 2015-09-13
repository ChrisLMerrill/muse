package org.musetest.core.test;

import org.musetest.core.*;
import org.musetest.core.events.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class BaseMuseTestResult implements MuseTestResult
    {
    public BaseMuseTestResult(MuseTestResultStatus status)
        {
        _status = status;
        }

    public BaseMuseTestResult(MuseTestResultStatus status, MuseTest test, EventLog log)
        {
        _status = status;
        _test = test;
        _log = log;
        }

    @Override
    public MuseTestResultStatus getStatus()
        {
        return _status;
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

    private MuseTestResultStatus _status;
    private MuseTest _test;
    private EventLog _log;
    }


