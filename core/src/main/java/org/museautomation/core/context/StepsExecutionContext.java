package org.museautomation.core.context;

import org.museautomation.core.*;
import org.museautomation.core.step.*;
import org.museautomation.core.steptest.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public interface StepsExecutionContext extends MuseExecutionContext
    {
    StepExecutionContextStack getExecutionStack();
    StepLocator getStepLocator();
    }

