package org.musetest.core.events;

import org.musetest.core.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class TestErrorEventType extends EventType
	{
	@SuppressWarnings("WeakerAccess") // instantiated via reflection
	public TestErrorEventType()
		{
		super(TYPE_ID, "Test Execution Error");
		}

	public static MuseEvent create(String message)
		{
		MuseEvent event = new MuseEvent(TYPE_ID);
		event.setAttribute(MuseEvent.DESCRIPTION, message);
		event.addTag(MuseEvent.ERROR);
		return event;
		}

	public final static String TYPE_ID = "error";
	}
