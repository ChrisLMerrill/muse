package org.musetest.core.events;

import org.musetest.core.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class EndTestEvent extends MuseEvent
    {
    public EndTestEvent(MuseTestResult result)
        {
        super(MuseEventType.EndTest);
        _result = result;
        }

    @Override
    public String getDescription()
        {
        return _result.getOneLineDescription();
        }

    public MuseTestResult getResult()
        {
        return _result;
        }

    private MuseTestResult _result;
    }


