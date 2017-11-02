package org.musetest.core.events;

import org.musetest.core.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class ValueSourceResolvedEvent extends MuseEvent
    {
    public ValueSourceResolvedEvent(String description, Object value)
        {
        super(ValueSourceResolvedEventType.TYPE);
        _description = description;
        _value = value;
        }

    @Override
    public String getDescription()
        {
        return "Resolved " + _description + " = " + _value;
        }

    private String _description;
    private Object _value;

    public static class ValueSourceResolvedEventType extends EventType
	    {
	    @Override
	    public String getTypeId()
		    {
		    return TYPE_ID;
		    }

	    @Override
	    public String getName()
		    {
		    return "ValueSource Resolved";
		    }

	    public final static String TYPE_ID = "ValueResolved";
	    public final static EventType TYPE = new ValueSourceResolvedEventType();
	    }
    }