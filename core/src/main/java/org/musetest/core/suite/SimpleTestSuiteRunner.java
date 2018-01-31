package org.musetest.core.suite;

import org.jetbrains.annotations.*;
import org.musetest.core.*;
import org.musetest.core.datacollection.*;
import org.musetest.core.execution.*;
import org.musetest.core.plugins.*;
import org.musetest.core.resultstorage.*;
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
    public boolean execute(MuseProject project, MuseTestSuite suite, List<MusePlugin> plugins)
        {
        _project = project;
        _context = new DefaultTestSuiteExecutionContext(project, suite);

        List<MusePlugin> suite_plugins = setupPlugins(plugins);

        boolean suite_success = runTests(suite, suite_plugins);

        // TODO, shut down the suite plugins
        if (!savePluginData(suite_plugins))
        	suite_success = false;

        return suite_success;
        }

    @SuppressWarnings("WeakerAccess")  // intended to be overridden by implementations with more complex methods (e.g. parallel)
    protected boolean runTests(MuseTestSuite suite, List<MusePlugin> suite_plugins)
	    {
	    boolean suite_success = true;
	    final Iterator<TestConfiguration> tests = suite.getTests(_project);
	    while (tests.hasNext())
		    {
		    final TestConfiguration configuration = tests.next();
		    configuration.withinProject(_project);

		    for (MusePlugin plugin : suite_plugins)
			    configuration.addPlugin(plugin);
		    final boolean test_success = runTest(configuration);
		    if (!test_success)
			    suite_success = false;
		    }
	    return suite_success;
	    }

    @NotNull
    private List<MusePlugin> setupPlugins(List<MusePlugin> plugins)
	    {
	    if (plugins == null)
		    plugins = Collections.emptyList();

	    Plugins.setup(_context);

	    List<MusePlugin> suite_plugins = new ArrayList<>();
	    suite_plugins.addAll(plugins);
	    suite_plugins.addAll(_context.getPlugins());
	    return suite_plugins;
	    }

    private boolean savePluginData(List<MusePlugin> suite_plugins)
	    {
	    if (_output_path != null)
		    {
		    File output_folder = new File(_output_path);
		    for (MusePlugin plugin : suite_plugins)
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
						    LOG.error(String.format("Unable to write test result data (%s) to disk (%s)", data.getClass().getSimpleName(), output.getAbsolutePath()), e);
						    return false;
						    }
					    }
				    }
		    }
	    return true;
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
        // TODO emit event for starting the test within the suite

        configuration.withinProject(_project);
        SimpleTestRunner runner = new SimpleTestRunner(_project, configuration);
        if (_output != null)
	        runner.getExecutionContext().setVariable(SaveTestResultsToDisk.OUTPUT_FOLDER_VARIABLE_NAME, _output.getOutputFolderName(configuration), VariableScope.Execution);
        runner.runTest();
        return runner.completedNormally();
        }

    protected MuseProject _project;
    private String _output_path = null;
    @SuppressWarnings("WeakerAccess")  // available to subclasses
    protected TestSuiteOutputOnDisk _output = null;
    private TestSuiteExecutionContext _context;

    private final static Logger LOG = LoggerFactory.getLogger(SimpleTestSuiteRunner.class);
    }