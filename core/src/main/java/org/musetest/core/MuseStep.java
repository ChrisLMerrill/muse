package org.musetest.core;

import org.musetest.core.context.*;
import org.musetest.core.step.*;
import org.musetest.core.steptest.*;

/**
 * A step is (usually) executed within a test.
 *
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public interface MuseStep
    {
    /**
     * Execute the step within the supplied context.
     */
    StepExecutionResult execute(StepExecutionContext context) throws StepExecutionError;

    /**
     * Get the configuration that produced this step.
     */
    StepConfiguration getConfiguration();
    }

