package org.musetest.core.suite;

import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.datacollection.*;
import org.musetest.core.execution.*;
import org.musetest.core.resultstorage.*;
import org.musetest.core.suite.plugin.*;
import org.musetest.core.suite.plugins.*;
import org.musetest.core.test.*;
import org.musetest.core.variables.*;
import org.slf4j.*;

import java.io.*;
import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class SimpleTestSuiteRunner implements MuseTestSuiteRunner
    {
    @Override
    public boolean execute(MuseProject project, MuseTestSuite suite, List<TestSuitePlugin> plugins)
        {
        _project = project;
        if (plugins == null)
        	plugins = Collections.emptyList();

        List<TestSuitePlugin> suite_plugins = Collections.emptyList();
        try
	        {
	        suite_plugins = TestSuitePlugins.locateAutomatic(new BaseExecutionContext(project), suite);
	        }
        catch (MuseExecutionError e)
	        {
	        LOG.error("Unable to locate automatic plugins", e);
	        }
        suite_plugins.addAll(plugins);

        boolean suite_success = true;

        final Iterator<TestConfiguration> tests = suite.getTests(project);
        while (tests.hasNext())
	        {
	        final TestConfiguration configuration = tests.next();
	        configuration.withinProject(project);
	        for (TestSuitePlugin plugin : suite_plugins)
		        configuration.addPlugin(plugin);
	        final boolean test_success = runTest(configuration);
	        if (!test_success)
	        	suite_success = false;
	        }

        if (_output_path != null)
	        {
	        File output_folder = new File(_output_path);
	        for (TestSuitePlugin plugin : suite_plugins)
	        	if (plugin instanceof DataCollector)
			        {
			        final TestResultData data = ((DataCollector) plugin).getData();
			        if (data != null)
				        {
				        File output = new File(output_folder, data.suggestFilename());
				        try (FileOutputStream outstream = new FileOutputStream(output))
					        {
					        data.write(outstream);
					        }
				        catch (IOException e)
					        {
					        suite_success = false;
					        LOG.error(String.format("Unable to write test result data (%s) to disk (%s)", data.getClass().getSimpleName(), output.getAbsolutePath()), e);
					        }
				        }
			        }
	        }

        return suite_success;
        }

    @Override
    public void setOutputPath(String path)
	    {
	    _output_path = path;
	    _output = new TestSuiteOutputOnDisk(path);
	    }

    @SuppressWarnings("WeakerAccess")  // external API
    protected boolean runTest(TestConfiguration configuration)
        {
        configuration.withinProject(_project);
        SimpleTestRunner runner = new SimpleTestRunner(_project, configuration);
        if (_output != null)
	        runner.getExecutionContext().setVariable(SaveTestResultsToDisk.OUTPUT_FOLDER_VARIABLE_NAME, _output.getOutputFolderName(configuration), VariableScope.Execution);
        runner.runTest();
        return runner.completedNormally();

/*
        TestResult result = TestResult.find(runner.getExecutionContext());
        if (result != null)
            return result;
        if (runner.completedNormally())
            return TestResult.create(configuration.name(), configuration.test().getId(), "Completed normally.");
        else
	        return TestResult.create(configuration.name(), configuration.test().getId(), "Incomplete.", TestResult.FailureType.Error, "Test did not complete normally.");
*/
        }

    private MuseProject _project;
    private String _output_path = null;
    private TestSuiteOutputOnDisk _output = null;

    private final static Logger LOG = LoggerFactory.getLogger(SimpleTestSuiteRunner.class);
    }