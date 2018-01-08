package org.musetest.core.test;

import org.musetest.core.*;
import org.musetest.core.events.*;
import org.musetest.core.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class ScriptFailureEventType extends EventType
	{
	@SuppressWarnings("WeakerAccess") // instantiated via reflection
	public ScriptFailureEventType()
		{
		super(TYPE_ID, "Script Error");
		}

	public static MuseEvent create(String message, Throwable exception)
		{
		MuseEvent event = new MuseEvent(TYPE_ID);
		event.setAttribute(MuseEvent.DESCRIPTION, message);
		if (exception != null)
			event.setAttribute(STACKTRACE, new ExceptionStringifier().create(exception).toString());
		event.addTag(MuseEvent.ERROR);
		return event;
		}

	public final static String TYPE_ID = "script-fail";
	public final static EventType INSTANCE = new ScriptFailureEventType();
	@SuppressWarnings("WeakerAccess")  // public API
	public final static String STACKTRACE = "stacktrace";
	}
