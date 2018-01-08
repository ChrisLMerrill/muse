package org.musetest.core.events;

import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.step.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class StartStepEventType extends StepEventType
	{
	@SuppressWarnings("WeakerAccess")  // instantiated by reflection
	public StartStepEventType()
		{
		super(TYPE_ID, "Start Step");
		}

	@Override
	public String getDescription(MuseEvent event)
		{
		return "Starting: " + event.getAttributeAsString(STEP_DESCRIPTION);
		}

	public static MuseEvent create(StepConfiguration step, StepExecutionContext context)
		{
		MuseEvent event = StepEventType.create(TYPE_ID, step);
		event.setAttribute(STEP_DESCRIPTION, context.getProject().getStepDescriptors().get(step).getShortDescription(step));
		return event;
		}

	public final static String TYPE_ID = "start-step";
	public final static EventType INSTANCE = new StartStepEventType();
	}