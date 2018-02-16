package org.musetest.core.events;

import org.musetest.core.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class StartSuiteEventType extends EventType
	{
	@SuppressWarnings("WeakerAccess") // instantiated via reflection
	public StartSuiteEventType()
		{
		super(TYPE_ID, "Start Suite");
		}

	@Override
	public String getDescription(MuseEvent event)
		{
		return "Suite Starting";
		}

	public static MuseEvent create()
		{
		return new MuseEvent(TYPE_ID);
		}

	public final static String TYPE_ID = "start-suite";
	}