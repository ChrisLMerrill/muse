package org.musetest.core.events;

import org.musetest.core.*;

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

	public static MuseEvent create()
		{
		return new MuseEvent(TYPE_ID);
		}

	public final static String TYPE_ID = "end-suite";
	}