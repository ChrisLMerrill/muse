package org.musetest.core.events;

import org.musetest.core.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public abstract class EventType
	{
	public EventType()
		{
		}

	public EventType(String typeid, String name)
		{
		_typeid = typeid;
		_name = name;
		}

	/**
	 * A unique identifier for this type.
	 */
	public String getTypeId()
		{
		return _typeid;
		}

	/**
	 * A very short human-readable name for the type of event.
	 */
	public String getName()
		{
		return _name;
		}

	public String getDescription(MuseEvent event)
		{
		final Object value = event.getAttribute(MuseEvent.DESCRIPTION);
		return value == null ? null : value.toString();
		}

	private String _typeid;
	private String _name;
	}
