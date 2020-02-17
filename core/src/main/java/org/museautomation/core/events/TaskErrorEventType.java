package org.museautomation.core.events;

import org.museautomation.core.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class TaskErrorEventType extends EventType
	{
	@SuppressWarnings("WeakerAccess") // instantiated via reflection
	public TaskErrorEventType()
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
