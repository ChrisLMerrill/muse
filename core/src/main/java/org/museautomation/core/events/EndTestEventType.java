package org.museautomation.core.events;

import org.museautomation.core.*;

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

	@Override
	public String getDescription(MuseEvent event)
		{
		return "Test Complete";
		}

	public static MuseEvent create()
		{
		return new MuseEvent(TYPE_ID);
		}

	public static boolean isPass(MuseEvent event)
		{
		return !event.hasTag(MuseEvent.FAILURE);
		}

	public final static String TYPE_ID = "end-test";
	}