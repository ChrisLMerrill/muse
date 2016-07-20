package org.musetest.core.events;

import org.musetest.core.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class StartTestEvent extends MuseEvent
    {
    public StartTestEvent(MuseTest test)
        {
        super(MuseEventType.StartTest);
        _test = test;
        }

    @Override
    public String getDescription()
        {
        return "Starting test: " + _test.getDescription();
        }

    private MuseTest _test;
    }


