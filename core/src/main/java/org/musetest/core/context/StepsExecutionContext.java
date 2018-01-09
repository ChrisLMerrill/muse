package org.musetest.core.context;

import org.musetest.core.*;
import org.musetest.core.step.*;
import org.musetest.core.steptest.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public interface StepsExecutionContext extends MuseExecutionContext
    {
    StepExecutionContextStack getExecutionStack();
    StepLocator getStepLocator();
    }

