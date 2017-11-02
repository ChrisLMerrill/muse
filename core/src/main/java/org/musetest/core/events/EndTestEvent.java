package org.musetest.core.events;

import org.musetest.core.*;

import static org.musetest.core.events.EndTestEvent.EndTestEventType.TYPE_INSTANCE;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class EndTestEvent extends MuseEvent
    {
    public EndTestEvent(String description, boolean pass)
        {
        super(TYPE_INSTANCE);
		_description = description;
		_pass = pass;
        }

    @Override
    public String getDescription()
        {
        return _description;
        }

	@SuppressWarnings("unused")  // needed for JSON de/serialization
	private void setDescription(String description)
		{
		_description = description;
		}

	public boolean isPass()
		{
		return _pass;
		}

    private String _description;
    private boolean _pass;

    public static class EndTestEventType extends EventType
	    {
	    @Override
	    public String getTypeId()
		    {
		    return TYPE_ID;
		    }

	    @Override
	    public String getName()
		    {
		    return "End Test";
		    }

	    public final static String TYPE_ID = "EndTest";
	    public final static EventType TYPE_INSTANCE = new EndTestEventType();
	    }
    }