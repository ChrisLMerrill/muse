package org.museautomation.core.events;

import org.museautomation.core.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class StartTaskEventType extends EventType
	{
	@SuppressWarnings("WeakerAccess")  // instantiated by reflection
	public StartTaskEventType()
		{
		super(TYPE_ID, "Start Task");
		}

	@Override
	public String getDescription(MuseEvent event)
		{
		return "Start task: " + event.getAttribute(TASK_NAME);
		}

	public static MuseEvent create(String task_id, String name)
		{
		MuseEvent event = new MuseEvent(TYPE_ID);
		event.setAttribute(TASK_ID, task_id);
		event.setAttribute(TASK_NAME, name);
		event.setAttribute(START_TIME, System.currentTimeMillis());
		return event;
		}

	public final static String TYPE_ID = "start-task";
	public final static EventType INSTANCE = new StartTaskEventType();
	public final static String TASK_NAME = "name";
	public final static String TASK_ID = "task-id";
	private final static String START_TIME = "start-time";
	}