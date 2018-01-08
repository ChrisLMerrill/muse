package org.musetest.core.events;

import org.musetest.core.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class EndTestEventType extends EventType
	{
	@SuppressWarnings("WeakerAccess") // instantiated via reflection
	public EndTestEventType()
		{
		super(TYPE_ID, "End Test");
		}

	public static MuseEvent create(String description, boolean pass)
		{
		MuseEvent event = new MuseEvent(TYPE_ID);
		event.setAttribute(MuseEvent.DESCRIPTION, description);
		if (!pass)
			event.addTag(MuseEvent.FAILURE);
		return event;
		}

	public static boolean isPass(MuseEvent event)
		{
		return !event.hasTag(MuseEvent.FAILURE);
		}

	public final static String TYPE_ID = "end-test";
	}