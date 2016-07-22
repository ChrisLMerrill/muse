package org.musetest.core.context;

import org.musetest.core.*;
import org.musetest.core.test.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public interface TestExecutionContext extends MuseExecutionContext
    {
    /**
     * Get the named variable from the local variable scope.
     */
    Object getVariable(String name);

    /**
     * Set a variable in the local variable scope. The variable will only be visible to steps running
     * in the same scope. Function calls, for example, declare a new variable scope.
     */
    void setVariable(String name, Object value);

    void cleanup();  // cleanup test resources
    }

