package org.museautomation.builtins.step;

import org.museautomation.core.*;
import org.museautomation.core.events.*;
import org.museautomation.core.step.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class VerifyFailureEventType extends EventType
	{
	@SuppressWarnings("WeakerAccess") // instantiated via reflection
	public VerifyFailureEventType()
		{
		super(TYPE_ID, "Verify Failed");
		}

	public static MuseEvent create(StepConfiguration config, String message, boolean fatal)
		{
		MuseEvent event = StepEventType.create(TYPE_ID, config);
		event.setAttribute(MuseEvent.DESCRIPTION, message);
		event.addTag(MuseEvent.FAILURE);
		if (fatal)
			event.addTag(MuseEvent.TERMINATE);
		return event;
		}

	public final static String TYPE_ID = "verify-failed";
	public final static EventType INSTANCE = new VerifyFailureEventType();
	}