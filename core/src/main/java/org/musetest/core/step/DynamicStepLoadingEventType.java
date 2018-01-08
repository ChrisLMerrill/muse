package org.musetest.core.step;

import org.musetest.core.*;
import org.musetest.core.events.*;
import org.musetest.core.variables.*;

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
	public static List<StepConfiguration> getLoadedSteps(MuseExecutionContext context)
		{
		final Object value = context.getVariable(STEP_LIST_VAR, VariableScope.Execution);
		if (value instanceof List)
			return (List) value;
		return null;
		}

	public final static String TYPE_ID = "load-steps";
	public final static EventType INSTANCE = new DynamicStepLoadingEventType();

	@SuppressWarnings("WeakerAccess") // public API
	public final static String STEP_LIST = "steplist";
	@SuppressWarnings("WeakerAccess") // public API
	public final static String STEP_LIST_VAR = "_steplist";
	}