package org.musetest.core.events;

import org.musetest.core.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class StartTestEvent extends MuseEvent
    {
    public StartTestEvent(MuseTest test)
        {
        super(INSTANCE);
        _test = test;
        }

    @Override
    public String getDescription()
        {
        return "Starting test: " + _test.getDescription();
        }

    private MuseTest _test;

    public static class StartTestEventType extends EventType
	    {
	    @Override
	    public String getTypeId()
		    {
		    return "start-test";
		    }

	    @Override
	    public String getName()
		    {
		    return "Start Test";
		    }
	    }

    private final static EventType INSTANCE = new StartTestEventType();
    }