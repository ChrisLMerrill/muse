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

    /**
     * Creates a TestRunner that runs asynchronously. I.e. it returns immediately and runs on another thread.
     *
     * @param project The project to run in
     * @param test The test to run
     *
     * @return The runner
     */
    @SuppressWarnings("WeakerAccess")  // public API
    public static InteractiveTestRunner createInteractiveRunner(MuseProject project, MuseTest test)
        {
        if (!(test instanceof SteppedTest))
            throw new IllegalArgumentException("Can only create an interactive test runner for SteppedTests.");

        TestExecutionContext context = createContext(project, test);
        InteractiveTestRunner runner = new InteractiveTestRunner(project, (SteppedTest) test, context);
        context.addEventListener(new PauseOnFailureOrError(runner));
        return runner;
        }

    private static TestExecutionContext createContext(MuseProject project, MuseTest test)
        {
        TestExecutionContext context = new DefaultTestExecutionContext(project, test);
        if (test instanceof SteppedTest)
            context = new DefaultSteppedTestExecutionContext(context);
        return context;
        }
    }


