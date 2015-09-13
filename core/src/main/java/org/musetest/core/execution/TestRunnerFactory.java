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
     * Creates a TestRunner that meets the requested criteria.
     */
    public static TestRunner create(MuseProject project, MuseTest test, boolean synchronous, boolean interactive)
        {
        return create(project, test, new DefaultTestExecutionContext(project), synchronous, interactive);
        }

    public static TestRunner create(MuseProject project, MuseTest test, TestExecutionContext context, boolean synchronous, boolean interactive)
        {
        if (context == null)
            context = new DefaultTestExecutionContext(project);
        if (test instanceof SteppedTest)
            context = new DefaultSteppedTestExecutionContext(context);

        if (synchronous && interactive)
            throw new IllegalArgumentException("Cannot create a test runner that is both synchronous and interactive.");

        if (project == null && synchronous)
            return new SimpleTestRunner(null, test, context);
        else
            {
            if (synchronous)
                return new BlockingThreadedTestRunner(project, test, context);
            else if (interactive)
                {
                if (!(test instanceof SteppedTest))
                    throw new IllegalArgumentException("Can only create an interactive test runner for SteppedTests.");
                return new InteractiveTestRunner(project, (SteppedTest) test, context);
                }
            else
                return new ThreadedTestRunner(project, test, context);
            }
        }

    /**
     * Used by the TestSuiteRunner
     */
    public static MuseTestResult runTest(MuseProject project, MuseTest test, TestExecutionContext context)
        {
        TestRunner runner = create(project, test, context, true, false);
        runner.runTest();
        return runner.getResult();
        }

    /**
     * Convenience method for the simplest method of running a test and getting the result. This is useful for unit
     * tests and some command-line operations.
     */
    public static MuseTestResult runTest(MuseProject project, MuseTest test)
        {
        return runTest(project, test, null);
        }
    }


