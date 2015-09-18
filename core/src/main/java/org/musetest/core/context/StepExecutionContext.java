package org.musetest.core.context;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public interface StepExecutionContext
    {
    SteppedTestExecutionContext getTestExecutionContext();

    /**
     * Get the value of a variable that are specific to this step.  These variables will exist only until
     * the step completes execution.  For CompoundSteps, they will persist until the step returns an execution
     * result of COMPLETE. This is typically useful only for developers of compound steps that need to store
     * some state until the next call to shouldEnter().
     *
     * @param name Name of the variable
     * @return The value of the variable
     */
    Object getStepVariable(String name);

    void setStepVariable(String name, Object value);
    }

