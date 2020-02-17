package org.museautomation.core.step;

import org.museautomation.core.*;
import org.museautomation.core.context.*;
import org.museautomation.core.events.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class DynamicStepLoadingEventType extends EventType
	{
	@SuppressWarnings("WeakerAccess") // instantiated via reflection
	public DynamicStepLoadingEventType()
		{
		super(TYPE_ID, "Load Steps");
		}

	public static MuseEvent create(StepConfiguration step, List<StepConfiguration> steps)
		{
		MuseEvent event = StepEventType.create(TYPE_ID, step);
		List<Long> step_ids = new ArrayList<>();
		for (StepConfiguration loaded : steps)
			step_ids.add(loaded.getStepId());
		event.setAttribute(STEP_LIST, step_ids);
		return event;
		}

	@SuppressWarnings("unused")  // required for UI
	public static List<StepConfiguration> getLoadedSteps(MuseEvent event, MuseExecutionContext context)
		{
		List<Long> step_ids = (List<Long>) event.getAttribute(STEP_LIST);
		if (step_ids == null)
			throw new IllegalArgumentException("This event does not contain a step list.");

		SteppedTaskExecutionContext step_context = MuseExecutionContext.findAncestor(context, SteppedTaskExecutionContext.class);
		if (step_context == null)
			throw new IllegalArgumentException("Can only do this in a SteppedTestExecutionContext");

		List<StepConfiguration> steps = new ArrayList<>();
		for (Long step_id : step_ids)
			steps.add(step_context.getStepLocator().findStep(step_id));
		return steps;
		}

	public final static String TYPE_ID = "load-steps";
	public final static EventType INSTANCE = new DynamicStepLoadingEventType();

	@SuppressWarnings("WeakerAccess") // public API
	public final static String STEP_LIST = "steplist";
	}