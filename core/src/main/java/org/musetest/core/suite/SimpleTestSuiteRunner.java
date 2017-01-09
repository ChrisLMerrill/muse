package org.musetest.core.suite;

import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.execution.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class SimpleTestSuiteRunner implements MuseTestSuiteRunner
    {
    public SimpleTestSuiteRunner(MuseTestSuite suite)
        {
        _suite = suite;
        }

    @Override
    public MuseTestSuiteResult execute(MuseProject project)
        {
        BaseMuseTestSuiteResult suite_result = new BaseMuseTestSuiteResult();
        for (TestConfiguration config : _suite.generateTestList(project))
            suite_result.addTestResult(runTest(project, config));

        return suite_result;
        }

    private static MuseTestResult runTest(MuseProject project, TestConfiguration configuration)
        {
        TestRunner runner = TestRunnerFactory.createSynchronousRunner(project, configuration.getTest());
        for (ContextInitializer initializer : configuration.getInitializers())
            runner.getExecutionContext().addInitializer(initializer);
        runner.runTest();
        return runner.getResult();
        }

    private MuseTestSuite _suite;
    }


