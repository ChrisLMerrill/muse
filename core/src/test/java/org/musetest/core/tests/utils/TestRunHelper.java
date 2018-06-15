package org.musetest.core.tests.utils;

import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.execution.*;
import org.musetest.core.plugins.*;
import org.musetest.core.test.*;
import org.musetest.core.test.plugins.*;

/**
 * Provides simple shims for running a MuseTest for unit testing.
 *
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class TestRunHelper
    {
    /**
     * Convenience method for the simplest method of running a test and getting the result. This is useful for unit
     * tests and some command-line operations.
     *
     * @param project The project to run in
     * @param test The test to run
     *
     * @return the test result
     */
    public static TestExecutionContext runTestReturnContext(MuseProject project, MuseTest test)
        {
        BasicTestConfiguration config = new BasicTestConfiguration(test);
        TestRunner runner = new SimpleTestRunner(new ProjectExecutionContext(project), config);
        config.addPlugin(new TestResultCollectorConfiguration().createPlugin());
        runner.runTest();
        return runner.getExecutionContext();
        }

    public static TestResult runTest(MuseProject project, MuseTest test)
        {
        BasicTestConfiguration config = new BasicTestConfiguration(test);
        TestRunner runner = new SimpleTestRunner(new ProjectExecutionContext(project), config);
        config.addPlugin(new TestResultCollectorConfiguration().createPlugin());
        runner.runTest();
        return TestResult.find(runner.getExecutionContext());
        }

    public static TestResult runTest(MuseProject project, MuseTest test, MusePlugin plugin)
        {
        BasicTestConfiguration config = new BasicTestConfiguration(test);
        config.addPlugin(new TestResultCollectorConfiguration().createPlugin());
        config.addPlugin(plugin);
        TestRunner runner = new SimpleTestRunner(new ProjectExecutionContext(project), config);
        runner.runTest();
        return TestResult.find(config.context());
        }

    public static TestResult runTest(MuseProject project, TestConfiguration config, MusePlugin plugin)
        {
        config.addPlugin(new TestResultCollectorConfiguration().createPlugin());
        if (plugin != null)
            config.addPlugin(plugin);
        TestRunner runner = new SimpleTestRunner(new ProjectExecutionContext(project), config);
        runner.runTest();
        return TestResult.find(config.context());
        }
    }
