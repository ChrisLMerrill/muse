package org.museautomation.core.events;

import org.museautomation.core.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class EndSuiteEventType extends EventType
	{
	@SuppressWarnings("WeakerAccess") // instantiated via reflection
	public EndSuiteEventType()
		{
		super(TYPE_ID, "End Suite");
		}

	@Override
	public String getDescription(MuseEvent event)
		{
		return "Suite Complete";
		}

	public static MuseEvent create(MuseTestSuite suite)
		{
		final MuseEvent event = new MuseEvent(TYPE_ID);
		event.setAttribute(MuseEvent.DESCRIPTION, suite.getId());
		return event;
		}

	public final static String TYPE_ID = "end-suite";
	}