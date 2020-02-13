package org.museautomation.core.events;

import org.museautomation.core.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class ValueSourceResolvedEventType extends EventType
	{
	@SuppressWarnings("WeakerAccess") // instantiated via reflection
	public ValueSourceResolvedEventType()
		{
		super(TYPE_ID, "ValueSource Resolved");
		}

	@Override
	public String getDescription(MuseEvent event)
		{
		return "Resolved " + event.getAttribute(SOURCE_DESCRIPTION) + " = " + event.getAttribute(VALUE_STRING);
		}

	public static MuseEvent create(String source_desc, Object value)
		{
		MuseEvent event = new MuseEvent(TYPE_ID);
		event.setAttribute(SOURCE_DESCRIPTION, source_desc);
		if (value == null)
			event.setAttribute(VALUE_STRING, null);
		else
			event.setAttribute(VALUE_STRING, value.toString());
		return event;
		}

	public final static String TYPE_ID = "value-resolved";
	public final static EventType INSTANCE = new ValueSourceResolvedEventType();

	@SuppressWarnings("WeakerAccess")  // public API
	public final static String SOURCE_DESCRIPTION = "sourcedesc";
	@SuppressWarnings("WeakerAccess")  // public API
	public final static String VALUE_STRING = "valuestr";
	}