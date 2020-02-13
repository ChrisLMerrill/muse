package org.museautomation.core.test;

import org.museautomation.core.*;
import org.museautomation.core.events.*;
import org.museautomation.core.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class ScriptFailureEventType extends EventType
	{
	@SuppressWarnings("WeakerAccess") // instantiated via reflection
	public ScriptFailureEventType()
		{
		super(TYPE_ID, "Script Failure");
		}

	public static MuseEvent create(String message, Throwable exception)
		{
		MuseEvent event = new MuseEvent(TYPE_ID);
		event.setAttribute(MuseEvent.DESCRIPTION, message);
		if (exception != null)
			event.setAttribute(STACKTRACE, new ExceptionStringifier().create(exception).toString());
		event.addTag(MuseEvent.FAILURE);
		return event;
		}

	public final static String TYPE_ID = "script-fail";
	public final static EventType INSTANCE = new ScriptFailureEventType();
	@SuppressWarnings("WeakerAccess")  // public API
	public final static String STACKTRACE = "stacktrace";
	}
