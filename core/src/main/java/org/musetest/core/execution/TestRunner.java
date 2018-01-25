package org.musetest.core.execution;

import org.musetest.core.context.*;

/**
 * Runs the test. If this is a synchronous runner, it will block until the test completes (the test
 * _might_ be run on a separate thread, depending on the specfic implementation).
 *
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public interface TestRunner
    {
    TestExecutionContext getExecutionContext();
    void runTest();

    /**
     * Returns true if the test completed normally, false if it did not. Null if the test has not yet completed.
     */
    Boolean completedNormally();
    }

