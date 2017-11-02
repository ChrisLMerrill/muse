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

    /**
     * This is a convenience method only. Use getTypeId() and EventTypes.findType() to lookup the EventType.
     *
     * This method will work as expected if the event was created within this JVM session. It will not work for
     * event types defined in extensions after de-serializing a saved event.
     */
    @Deprecated
    public EventType getType()
        {
        if (_type != null)
            return _type;
        else
	        {
	        _type = EventTypes.DEFAULT.findType(_type_id);  // this is a hack without a reference to the project
	        return _type;
	        }
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
        return getType().getName();
        }

    public long getTimestampNanos()
        {
        return _timestamp_nanos;
        }

    private transient EventType _type;
    protected String _type_id = null;
    protected long _timestamp_nanos = System.nanoTime();

    private final static DefaultClassLocator DEFAULT_LOCATOR = new DefaultClassLocator();
    }


