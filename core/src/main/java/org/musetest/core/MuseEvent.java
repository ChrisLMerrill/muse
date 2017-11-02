package org.musetest.core;

import org.musetest.core.events.*;
import org.musetest.core.resource.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class MuseEvent
    {
    public MuseEvent(EventType type)
        {
        _type = null;
        _type_id = type.getTypeId();
        }

    public EventType getType()
        {
        if (_type != null)
            return _type;
        else
	        return EventTypes.DEFAULT.findType(_type_id);
        }

    public String getTypeId()
	    {
	    if (_type != null)
	    	return _type.getName();
	    else
	    	return _type_id;
	    }

    public String getDescription()
        {
        return _type.getName();
        }

    public long getTimestampNanos()
        {
        return _timestamp_nanos;
        }

    private transient final EventType _type;
    protected String _type_id = null;
    protected long _timestamp_nanos = System.nanoTime();

    private final static DefaultClassLocator DEFAULT_LOCATOR = new DefaultClassLocator();
    }


