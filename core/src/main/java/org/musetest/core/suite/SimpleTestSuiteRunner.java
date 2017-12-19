package org.musetest.core.suite;

import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.execution.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class SimpleTestSuiteRunner implements MuseTestSuiteRunner
    {
    @Override
    public MuseTestSuiteResult execute(MuseProject project, MuseTestSuite suite)
        {
        _project = project;
        _suite = suite;
        BaseMuseTestSuiteResult suite_result = new BaseMuseTestSuiteResult(_suite);
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
        MuseTestResult result = runner.getResult();
        result.setConfiguration(configuration);
        return result;
        }

    private MuseTestSuite _suite;
    private MuseProject _project;
    }


