package org.musetest.core.tests.utils;

import org.musetest.core.*;
import org.musetest.core.execution.*;
import org.musetest.core.test.*;
import org.musetest.core.test.plugin.*;

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
    public static MuseTestResult runTest(MuseProject project, MuseTest test)
        {
        TestRunner runner = new SimpleTestRunner(project, test);
        runner.runTest();
        return runner.getResult();
        }

    public static MuseTestResult runTest(MuseProject project, MuseTest test, TestPlugin plugin)
        {
        BasicTestConfiguration config = new BasicTestConfiguration(test);
        config.addPlugin(plugin);
        TestRunner runner = new SimpleTestRunner(project, config);
        runner.runTest();
        return runner.getResult();
        }
    }
