package org.musetest.core.suite;

import org.musetest.core.*;
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
            suite_result.addTestResult(TestRunnerFactory.runTest(project, config._test, config._context));

        return suite_result;
        }

    private MuseTestSuite _suite;
    }


