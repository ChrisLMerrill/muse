package org.musetest.core.test;

import org.musetest.core.*;
import org.musetest.core.events.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class ScriptFailureEvent extends MuseEvent
    {
    public ScriptFailureEvent(String message, @SuppressWarnings("unused") Throwable exception)
        {
        super(ScriptFailureEventType.INSTANCE);
        _message = message;
        }

    @Override
    public String getDescription()
	    {
	    return _message;
	    }

    private String _message;

    public final static class ScriptFailureEventType extends EventType
	    {
	    @Override
	    public String getTypeId()
		    {
		    return TYPE_ID;
		    }

	    @Override
	    public String getName()
		    {
		    return "Script Error";
		    }

	    public final static String TYPE_ID = "script-fail";
	    public final static EventType INSTANCE = new ScriptFailureEventType();
	    }

    }