package org.musetest.core.execution;

import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.steptest.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class TestRunnerFactory
    {
    /**
     * Creates a TestRunner that waits for the test to complete before returning.
     *
     * @param project The project to run in
     * @param test The test to run
     *
     * @return The runner
     */
    public static TestRunner createSynchronousRunner(MuseProject project, MuseTest test)
        {
        TestExecutionContext context = createContext(project, test);
        BlockingThreadedTestRunner runner = new BlockingThreadedTestRunner(project, test, context);
        context.addEventListener(new TerminateOnError(runner));
        return runner;
        }

    @SuppressWarnings("WeakerAccess")  // public API
    public static TestExecutionContext createContext(MuseProject project, MuseTest test)
        {
        TestExecutionContext context = new DefaultTestExecutionContext(project, test);
        if (test instanceof SteppedTest)
            context = new DefaultSteppedTestExecutionContext(context);
        return context;
        }
    }


