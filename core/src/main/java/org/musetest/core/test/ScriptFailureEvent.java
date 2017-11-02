package org.musetest.core.test;

import org.musetest.core.*;
import org.musetest.core.events.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class ScriptFailureEvent extends MuseEvent
    {
    public ScriptFailureEvent(String message, Throwable exception)
        {
        super(ScriptFailureEventType.TYPE);
        _message = message;
        _exception = exception;
        }

    private String _message;
    private String _script;
    private Throwable _exception;

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

	    public final static String TYPE_ID = "ScriptError";
	    public final static EventType TYPE = new ScriptFailureEventType();
	    }

    }