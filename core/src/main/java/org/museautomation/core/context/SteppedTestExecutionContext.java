package org.museautomation.core.context;

import org.museautomation.core.steptest.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public interface SteppedTestExecutionContext extends TestExecutionContext, StepsExecutionContext
    {
    @Override
    SteppedTest getTest();
    }

