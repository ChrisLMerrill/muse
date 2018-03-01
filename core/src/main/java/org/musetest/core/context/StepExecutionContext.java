package org.musetest.core.context;

import org.musetest.core.*;
import org.musetest.core.resource.*;
import org.musetest.core.step.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public interface StepExecutionContext extends StepsExecutionContext
    {
    /**
     * Get the configuration source of the current step.
     */
    StepConfiguration getCurrentStepConfiguration();

    /**
     * Get the current step, creating it if not yet available.
     */
    MuseStep getCurrentStep() throws MuseInstantiationException;

    /**
     * Called by the executor when the step is complete.
     */
    void stepComplete(MuseStep step, StepExecutionResult result);

    /**
     * Return true if this context has another step to execute.
     */
    boolean hasStepToExecute();

    @Override
    StepsExecutionContext getParent();
    }