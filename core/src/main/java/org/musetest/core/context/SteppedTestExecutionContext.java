package org.musetest.core.context;

import org.musetest.core.steptest.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public interface SteppedTestExecutionContext extends TestExecutionContext
    {
    StepExecutionContextStack getExecutionStack();
    }

