package org.musetest.core.events;

import org.musetest.core.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class MessageEvent extends MuseEvent
    {
    public MessageEvent(String message)
        {
        super(MessageEventType.TYPE);
        _message = message;
        }

    @Override
    public String getDescription()
        {
        return _message;
        }

    private String _message;

    public final static class MessageEventType extends EventType
	    {
	    @Override
	    public String getTypeId()
		    {
		    return TYPE_ID;
		    }

	    @Override
	    public String getName()
		    {
		    return "Message";
		    }

	    public final static String TYPE_ID = "Message";
	    public final static EventType TYPE = new MessageEventType();
	    }

    }


