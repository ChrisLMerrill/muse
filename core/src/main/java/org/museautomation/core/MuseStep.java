package org.museautomation.core;

import org.museautomation.core.context.*;
import org.museautomation.core.step.*;
import org.museautomation.core.steptest.*;

/**
 * A step is (usually) executed within a test.
 *
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public interface MuseStep
    {
    /**
     * @param context The context in which to execute this step.
     * @return The result of the step execution.
     * @throws StepExecutionError if an configuration error or other bug prevents the step from executing
     */
    StepExecutionResult execute(StepExecutionContext context) throws MuseExecutionError;

    /**
     * @return The configuration that produced this step.
     */
    StepConfiguration getConfiguration();
    }

