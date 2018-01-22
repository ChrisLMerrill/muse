package org.musetest.core.events;

import org.musetest.core.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class StartTestEventType extends EventType
	{
	@SuppressWarnings("WeakerAccess")  // instantiated by reflection
	public StartTestEventType()
		{
		super(TYPE_ID, "Start Test");
		}

	@Override
	public String getDescription(MuseEvent event)
		{
		return "Starting test: " + event.getAttribute(MuseEvent.DESCRIPTION);
		}

	public static MuseEvent create(MuseTest test, String name)
		{
		MuseEvent event = new MuseEvent(TYPE_ID);
		event.setAttribute(MuseEvent.DESCRIPTION, test.getDescription());
		event.setAttribute(TEST_ID, test.getId());
		event.setAttribute(TEST_NAME, name);
		return event;
		}

	public final static String TYPE_ID = "start-test";
	public final static EventType INSTANCE = new StartTestEventType();
	public final static String TEST_NAME = "name";
	public final static String TEST_ID = "test-id";
	}