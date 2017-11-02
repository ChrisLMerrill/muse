package org.musetest.core.events;

import org.musetest.core.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class StartTestEvent extends MuseEvent
    {
    public StartTestEvent(MuseTest test)
        {
        super(TYPE);
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
		    return "StartTest";
		    }

	    @Override
	    public String getName()
		    {
		    return "Start Test";
		    }
	    }
    private static EventType TYPE = new StartTestEventType();
    }