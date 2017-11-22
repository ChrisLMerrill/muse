package org.musetest.core.step;

import org.musetest.core.context.*;
import org.musetest.core.events.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class DynamicStepLoadingEvent extends StepEvent
    {
    @SuppressWarnings("WeakerAccess")  // used in UI tests
    public DynamicStepLoadingEvent(StepConfiguration config, StepExecutionContext context, List<StepConfiguration> steps)
        {
        super(DynamicStepLoadEventType.INSTANCE, config, context);
        _steps = steps;
        }

    @SuppressWarnings("unused")  // used in UI
    public List<StepConfiguration> getLoadedSteps()
        {
        return _steps;
        }

    private List<StepConfiguration> _steps;

    public final static class DynamicStepLoadEventType extends EventType
	    {
	    @Override
	    public String getTypeId()
		    {
		    return TYPE_ID;
		    }

	    @Override
	    public String getName()
		    {
		    return "Load Steps";
		    }

	    public final static String TYPE_ID = "load-steps";
	    final static EventType INSTANCE = new DynamicStepLoadEventType();
	    }

    }