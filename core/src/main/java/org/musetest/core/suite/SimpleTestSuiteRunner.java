package org.musetest.core.suite;

import org.musetest.core.*;
import org.musetest.core.execution.*;
import org.musetest.core.test.plugins.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class SimpleTestSuiteRunner implements MuseTestSuiteRunner
    {
    @Override
    public MuseTestSuiteResult execute(MuseProject project, MuseTestSuite suite)
        {
        _project = project;
        BaseMuseTestSuiteResult suite_result = new BaseMuseTestSuiteResult(suite);
        for (TestConfiguration config : suite.generateTestList(project))
            suite_result.addTestResult(runTest(config));

        return suite_result;
        }

    @SuppressWarnings("WeakerAccess")  // external API
    protected MuseTestResult runTest(TestConfiguration configuration)
        {
        TestRunner runner = TestRunnerFactory.createSynchronousRunner(_project, configuration.getTest());
        for (TestPlugin plugin : configuration.getPlugins())
            runner.getExecutionContext().addTestPlugin(plugin);
        runner.runTest();
        MuseTestResult result = runner.getResult();
        result.setConfiguration(configuration);
        return result;
        }

    private MuseProject _project;
    }


