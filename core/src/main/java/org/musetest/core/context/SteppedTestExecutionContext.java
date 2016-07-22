package org.musetest.core.context;

import org.musetest.core.steptest.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public interface SteppedTestExecutionContext extends TestExecutionContext
    {
    /**
     * Get the value of the named test variable.
     */
    Object getLocalVariable(String name);

    /**
     * Set a variable that is visible only to the local variable scope.
     */
    void setLocalVariable(String name, Object value);

    StepExecutionContextStack getExecutionStack();
    }

