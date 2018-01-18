package org.musetest.core.suite;

import org.musetest.core.*;
import org.musetest.core.execution.*;
import org.musetest.core.resultstorage.*;
import org.musetest.core.suite.plugin.*;
import org.musetest.core.test.*;
import org.musetest.core.variables.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class SimpleTestSuiteRunner implements MuseTestSuiteRunner
    {
    @Override
    public MuseTestSuiteResult execute(MuseProject project, MuseTestSuite suite, List<TestSuitePlugin> plugins)
        {
        _project = project;
        BaseMuseTestSuiteResult suite_result = new BaseMuseTestSuiteResult(suite);
        final Iterator<TestConfiguration> tests = suite.getTests(project);
        while (tests.hasNext())
	        suite_result.addTestResult(runTest(tests.next()));

        return suite_result;
        }

    @Override
    public void setOutputPath(String path)
	    {
	    _output = new TestSuiteOutputOnDisk(path);
	    }

    @SuppressWarnings("WeakerAccess")  // external API
    protected MuseTestResult runTest(TestConfiguration configuration)
        {
        SimpleTestRunner runner = new SimpleTestRunner(_project, configuration);
        if (_output != null)
	        runner.getExecutionContext().setVariable(SaveTestResultsToDisk.OUTPUT_FOLDER_VARIABLE_NAME, _output.getOutputFolderName(configuration), VariableScope.Execution);
        runner.runTest();
        MuseTestResult result = runner.getResult();
        result.setConfiguration(configuration);
        return result;
        }

    private MuseProject _project;
    private TestSuiteOutputOnDisk _output = null;
    }