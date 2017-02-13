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
        _project = project;
        BaseMuseTestSuiteResult suite_result = new BaseMuseTestSuiteResult();
        for (TestConfiguration config : _suite.generateTestList(project))
            suite_result.addTestResult(runTest(config));

        return suite_result;
        }

    @SuppressWarnings("WeakerAccess")  // external API
    protected MuseTestResult runTest(TestConfiguration configuration)
        {
        TestRunner runner = TestRunnerFactory.createSynchronousRunner(_project, configuration.getTest());
        for (ContextInitializer initializer : configuration.getInitializers())
            runner.getExecutionContext().addInitializer(initializer);
        runner.runTest();
        return runner.getResult();
        }

    private MuseTestSuite _suite;
    private MuseProject _project;
    }


