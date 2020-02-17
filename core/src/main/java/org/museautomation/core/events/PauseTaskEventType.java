package org.museautomation.core.events;

import org.museautomation.core.*;
import org.museautomation.core.step.*;

import static org.museautomation.core.events.StepEventType.STEP_DESCRIPTION;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@SuppressWarnings("unused")  // public API
public class PauseTaskEventType extends EventType
	{
	@SuppressWarnings("WeakerAccess")  // instantiated by reflection
	public PauseTaskEventType()
		{
		super(TYPE_ID, "Pause");
		}

	@Override
	public String getDescription(MuseEvent event)
		{
		return "Paused at: " + event.getAttributeAsString(STEP_DESCRIPTION);
		}

	public static MuseEvent create(StepConfiguration step, MuseExecutionContext context)
		{
		final MuseEvent event = StepEventType.create(TYPE_ID, step);
		event.setAttribute(STEP_DESCRIPTION, context.getProject().getStepDescriptors().get(step).getShortDescription(step));
		return event;
		}

	public final static String TYPE_ID = "pause";
	public final static EventType INSTANCE = new PauseTaskEventType();
	}


