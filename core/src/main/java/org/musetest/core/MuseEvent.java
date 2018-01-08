package org.musetest.core;

import org.musetest.core.events.*;

import java.util.*;

/**
 * Represents an event recorded during execution of Muse steps.
 * Note that this corresponds to a specific serialized format. Hence, it is final. You should extend EventType instead.
 *
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public final class MuseEvent
    {
    public MuseEvent(EventType type)
        {
        _type_id = type.getTypeId();
        }

    public MuseEvent(String typeid)
        {
        _type_id = typeid;
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
	    	return _type.getTypeId();
	    else
	    	return _type_id;
	    }

    @Deprecated
    public String getDescription()
        {
        final Object attribute = getAttribute(DESCRIPTION);
        return attribute == null ? _type_id : attribute.toString();
        }

    @Deprecated
    public EventStatus getStatus()
	    {
	    if (_status == null)
		    _status = EventStatus.Normal;
	    return _status;
	    }

    @Deprecated
    public void setStatus(EventStatus status)
	    {
	    _status = status;
	    }

    @Deprecated
    public boolean isTerminateRequested()
        {
        return _terminate;
        }

    @Deprecated
    public void setTerminate(boolean terminate)
	    {
	    _terminate = terminate;
	    }

    public long getTimestampNanos()
        {
        return _timestamp_nanos;
        }

    public void setTimestampNanos(long  nanos)
        {
        _timestamp_nanos = nanos;
        }

    public void addTag(String tag)
	    {
	    if (_tags == null)
	    	_tags = new ArrayList<>();
	    if (!_tags.contains(tag))
			_tags.add(tag);
	    }

    public boolean hasTag(String tag)
	    {
	    if (_tags == null)
	    	return false;
	    return _tags.contains(tag);
	    }

    public void setAttribute(String name, Object value)
	    {
	    if (_attributes == null)
	    	_attributes = new HashMap<>();
	    _attributes.put(name, value);
	    }

    public Object getAttribute(String name)
	    {
	    if (_attributes == null)
	    	return null;
	    return _attributes.get(name);
	    }

    public String getAttributeAsString(String name)
	    {
	    if (_attributes == null)
	    	return null;
	    final Object attribute = _attributes.get(name);
	    return attribute == null ? null : attribute.toString();
	    }

    public boolean hasAttribute(String name, Object value)
	    {
	    return value.equals(getAttribute(name));
	    }

    /**
     * Intended only for de/serialization.
     */
    @SuppressWarnings("unused")
    public Map<String, Object> getAttributes()
	    {
	    if (_attributes == null || _attributes.isEmpty())
	        return null;
        return _attributes;
	    }

    /**
     * Intended only for de/serialization.
     */
    @SuppressWarnings("unused")
    public void setAttributes(Map<String, Object> attributes)
	    {
	    _attributes = attributes;
	    }

    /**
     * Intended only for de/serialization.
     */
    @SuppressWarnings("unused")
    public List<String> getTags()
	    {
	    if (_tags == null || _tags.isEmpty())
	        return null;
        return _tags;
	    }

    /**
     * Intended only for de/serialization.
     */
    @SuppressWarnings("unused")
    public void setTags(List<String> tags)
	    {
	    _tags = tags;
	    }

    private String _type_id;
    private long _timestamp_nanos = System.nanoTime();
    private Map<String, Object> _attributes = null;
    private List<String> _tags = null;

    public final static String TERMINATE = "terminate";
    public final static String ERROR = "error";
    public final static String FAILURE = "failure";
    public final static String DESCRIPTION = "description";

    @Deprecated
    private EventStatus _status = EventStatus.Normal;

    @Deprecated
    private boolean _terminate = false;

    @Deprecated
    private transient EventType _type;
    }