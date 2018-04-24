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
		return "Start test: " + event.getAttribute(TEST_NAME);
		}

	public static MuseEvent create(String test_id, String name)
		{
		MuseEvent event = new MuseEvent(TYPE_ID);
		event.setAttribute(TEST_ID, test_id);
		event.setAttribute(TEST_NAME, name);
		event.setAttribute(START_TIME, System.currentTimeMillis());
		return event;
		}

	public final static String TYPE_ID = "start-test";
	public final static EventType INSTANCE = new StartTestEventType();
	public final static String TEST_NAME = "name";
	public final static String TEST_ID = "test-id";
	private final static String START_TIME = "start-time";
	}