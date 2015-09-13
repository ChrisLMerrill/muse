package org.musetest.core.events;

import org.musetest.core.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class EndTestEvent extends MuseEvent
    {
    public EndTestEvent(MuseTest test, MuseTestResult result)
        {
        super(MuseEventType.EndTest);
        _test = test;
        _result = result;
        }

    @Override
    public String getDescription()
        {
        return "Finished test: " + _test.getDescription() + ", result=" + _result.getStatus();
        }

    public MuseTestResult getResult()
        {
        return _result;
        }

    MuseTest _test;
    MuseTestResult _result;
    }


