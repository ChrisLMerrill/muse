package org.musetest.core.context;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public interface StepExecutionContext
    {
    SteppedTestExecutionContext getTestExecutionContext();

    /**
     * Get and set variables that are specific to this step.  These variables will exist only until
     * the step completes execution.  For CompoundSteps, they will persist until the step returns an execution
     * result of COMPLETE.
     */
    Object getStepVariable(String name);
    void setStepVariable(String name, Object value);
    }

