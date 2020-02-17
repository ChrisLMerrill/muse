package org.museautomation.core.events;

import org.museautomation.core.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class EndTaskEventType extends EventType
	{
	@SuppressWarnings("WeakerAccess") // instantiated via reflection
	public EndTaskEventType()
		{
		super(TYPE_ID, "End Task");
		}

	@Override
	public String getDescription(MuseEvent event)
		{
		return "Task Complete";
		}

	public static MuseEvent create()
		{
		return new MuseEvent(TYPE_ID);
		}

	public static boolean isPass(MuseEvent event)
		{
		return !event.hasTag(MuseEvent.FAILURE);
		}

	public final static String TYPE_ID = "end-task";
	}