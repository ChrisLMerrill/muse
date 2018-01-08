package org.musetest.builtins.step;

import org.musetest.core.*;
import org.musetest.core.events.*;
import org.musetest.core.step.*;

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
		event.setAttribute(EndStepEventType.STEP_FAILURE_DESCRIPTION, message);
		event.addTag(MuseEvent.FAILURE);
		if (fatal)
			event.addTag(MuseEvent.TERMINATE);
		return event;
		}

	public final static String TYPE_ID = "verify-failed";
	public final static EventType INSTANCE = new VerifyFailureEventType();
	}