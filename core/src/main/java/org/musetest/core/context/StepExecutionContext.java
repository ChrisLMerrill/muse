package org.musetest.core.context;

import org.musetest.core.*;
import org.musetest.core.resource.*;
import org.musetest.core.step.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public interface StepExecutionContext
    {
    /**
     * Get the context in which the entire test is being executed.
     */
    SteppedTestExecutionContext getTestExecutionContext();

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

    /**
     * Get the value of a variable in the current variable scope.
     */
    Object getVariable(String name);

    /**
     * Set the value of a variable in the current variable scope.
     */
    void setVariable(String name, Object value);

    /**
     * Get the map of all variables. May be null if this context does not define a new variable scope.
     *
     * Steps should NEVER use this method to get or set variables. It is only for use by the other
     * contexts and tooling to allow inspection of all variables.
     */
    Map<String,Object> getVariables();
    }

