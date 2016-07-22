package org.musetest.core.context;

import org.musetest.core.*;
import org.musetest.core.resource.*;
import org.musetest.core.step.*;
import org.musetest.core.steptest.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public interface StepExecutionContext extends MuseExecutionContext
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
     * Get the value of a variable in the current variable scope.  If not found in the current scope, the
     * value from the test-wide scope will be returned (if it exists).
     */
    Object getLocalVariable(String name);

    /**
     * Set the value of a variable in the current variable scope. This will hide test-wide variables of
     * the same name from access within this scope.
     */
    void setLocalVariable(String name, Object value);

    /**
     * Get the map of all variables. May be null if this context does not define a new variable scope.
     *
     * Steps should NEVER use this method to get or set variables. It is only for use by the other
     * contexts and tooling to allow inspection of all variables.
     */
    Map<String,Object> getVariables();

    /**
     * The execution stack tracks hierarchical step context. Steps that affect that hierarchy (calling functions,
     * returning, looping, etc) may use this to effect their changes.
     */
    StepExecutionContextStack getExecutionStack();

    @Override
    SteppedTestExecutionContext getParent();
    }

