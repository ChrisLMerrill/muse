package org.musetest.core.events;

import org.musetest.core.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class TestErrorEvent extends MuseEvent
    {
    public TestErrorEvent(String message)
        {
        super(TYPE);
        _message = message;
        }

    public String getDescription()
        {
        return "Unable to execute test: " + _message;
        }

    private String _message;

    public static class TestErrorEventType extends EventType
	    {
	    @Override
	    public String getTypeId()
		    {
		    return "TestExecutionError";
		    }

	    @Override
	    public String getName()
		    {
		    return "Test Execution Error";
		    }
	    }
    private final static EventType TYPE = new TestErrorEventType();
    }


