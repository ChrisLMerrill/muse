package org.musetest.core.step;

import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.events.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class DynamicStepLoadingEvent extends StepEvent
    {
    public DynamicStepLoadingEvent(StepConfiguration config, StepExecutionContext context, List<StepConfiguration> steps)
        {
        super(MuseEventType.DynamicStepLoad, config, context);
        _steps = steps;
        }

    public List<StepConfiguration> getLoadedSteps()
        {
        return _steps;
        }

    private List<StepConfiguration> _steps;
    }


