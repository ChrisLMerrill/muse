package org.museautomation.core.events;

import org.museautomation.core.*;
import org.museautomation.core.context.*;
import org.museautomation.core.step.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class EndStepEventType extends StepEventType
	{
	public EndStepEventType()
		{
		super(TYPE_ID, "End Step");
		}

	@Override
	public String getDescription(MuseEvent event)
		{
		String description = "End: " + event.getAttributeAsString(STEP_DESCRIPTION);

		// look for error
		String error_message = null;
		if (event.hasTag(MuseEvent.ERROR))
			error_message = " -- ERROR: ";
		if (event.hasTag(MuseEvent.FAILURE))
			error_message = " -- FAILURE: ";

		if (error_message != null)
			description += error_message + event.getAttributeAsString(MuseEvent.DESCRIPTION);
		else if (event.hasTag(INCOMPLETE))
			description += " (incomplete)";
		
		return description;
		}

	public static MuseEvent create(StepConfiguration step, StepExecutionContext context, StepExecutionResult result)
		{
		MuseEvent event = StepEventType.create(TYPE_ID, step);
		event.setAttribute(STEP_DESCRIPTION, context.getProject().getStepDescriptors().get(step).getShortDescription(step));
		switch (result.getStatus())
			{
			case ERROR:
				event.addTag(MuseEvent.ERROR);
				event.setAttribute(MuseEvent.DESCRIPTION, result.getDescription());
				break;
			case FAILURE:
				event.addTag(MuseEvent.FAILURE);
				event.setAttribute(MuseEvent.DESCRIPTION, result.getDescription());
				break;
			case INCOMPLETE:
				event.addTag(StepEventType.INCOMPLETE);
			}
        // copy any additional attributes from the result into the event
        for (String name : result.metadata().getMetadataFieldNames())
            event.setAttribute(name, result.metadata().getMetadataField(name));
		return event;
		}

	public final static String TYPE_ID = "end-step";
	public final static EventType INSTANCE = new EndStepEventType();
	}