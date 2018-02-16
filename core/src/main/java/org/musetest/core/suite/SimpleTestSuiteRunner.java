package org.musetest.core.suite;

import org.jetbrains.annotations.*;
import org.musetest.core.*;
import org.musetest.core.datacollection.*;
import org.musetest.core.events.*;
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
    public boolean execute(MuseProject project, MuseTestSuite suite, List<MusePlugin> manual_plugins)
        {
        _project = project;
        _context = new DefaultTestSuiteExecutionContext(project, suite);

        List<MusePlugin> auto_plugins = Plugins.setup(_context);
        for (MusePlugin plugin : manual_plugins)
        	_context.addPlugin(plugin);

        try
	        {
	        _context.initializePlugins();
	        }
        catch (MuseExecutionError e)
	        {
	        _context.raiseEvent(TestErrorEventType.create("Unable to initialize test suite plugins due to: " + e.getMessage()));
	        return false;
	        }

        boolean suite_success = runTests(suite, manual_plugins, auto_plugins);

        // this is just for should be for the plugins installed into local context!
        if (!savePluginData(_context))
        	suite_success = false;

        return suite_success;
        }

    @SuppressWarnings("WeakerAccess")  // intended to be overridden by implementations with more complex methods (e.g. parallel)
    protected boolean runTests(MuseTestSuite suite, List<MusePlugin> manual_plugins, List<MusePlugin> auto_plugins)
	    {
	    _context.raiseEvent(StartSuiteEventType.create());
	    boolean suite_success = true;
	    final Iterator<TestConfiguration> tests = suite.getTests(_project);
	    while (tests.hasNext())
		    {
		    final boolean test_success = runTest(tests.next(), manual_plugins, auto_plugins);
		    if (!test_success)
			    suite_success = false;
		    }
	    _context.raiseEvent(EndSuiteEventType.create());
	    return suite_success;
	    }

    private boolean savePluginData(TestSuiteExecutionContext context)
	    {
	    if (_output_path != null)
		    {
		    File output_folder = new File(_output_path);
		    for (DataCollector collector : context.getDataCollectors())
			    {
			    final TestResultData data = collector.getData();
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
    protected boolean runTest(TestConfiguration configuration, List<MusePlugin> manual_plugins, List<MusePlugin> auto_plugins)
        {
        configuration.withinProject(_project);
        MuseExecutionContext test_context = configuration.context();
        setupTestPlugins(manual_plugins, auto_plugins, test_context);

        SimpleTestRunner runner = createRunner(configuration);
        if (_output != null)
	        runner.getExecutionContext().setVariable(SaveTestResultsToDisk.OUTPUT_FOLDER_VARIABLE_NAME, _output.getOutputFolderName(configuration), VariableScope.Execution);
        MuseEvent start_event = raiseTestStartEvent(configuration);
        runner.runTest();
        raiseTestEndEvent(start_event);
        return runner.completedNormally();
        }

    protected MuseEvent raiseTestStartEvent(TestConfiguration configuration)
	    {
	    final MuseEvent start_event = StartSuiteTestEventType.create(_context.createVariable("testconfig", configuration));
	    _context.raiseEvent(start_event);
	    return start_event;
	    }

	protected void raiseTestEndEvent(MuseEvent start_event)
		{
		final String config_var_name = StartSuiteTestEventType.getConfigVariableName(start_event);
		final MuseEvent end_event = EndSuiteTestEventType.create(config_var_name);
		_context.raiseEvent(end_event);
        _context.setVariable(config_var_name, null);
		}

    @NotNull
    @SuppressWarnings("WeakerAccess")  // overridden in GUI.
    protected SimpleTestRunner createRunner(TestConfiguration configuration)
	    {
	    return new SimpleTestRunner(_project, configuration);
	    }

    @SuppressWarnings("WeakerAccess")  // used by subclasses in extensions
    protected void setupTestPlugins(List<MusePlugin> manual_plugins, List<MusePlugin> auto_plugins, MuseExecutionContext test_context)
	    {
	    for (MusePlugin plugin : auto_plugins)
		    try
			    {
			    plugin.conditionallyAddToContext(test_context, true);
			    }
		    catch (MuseExecutionError e)
			    {
			    test_context.raiseEvent(TestErrorEventType.create("Unable to install plugin " + plugin.getClass().getSimpleName() + " due to " + e.getMessage()));
			    }
	    for (MusePlugin plugin : manual_plugins)
		    try
			    {
			    plugin.conditionallyAddToContext(test_context, false);
			    }
		    catch (MuseExecutionError e)
			    {
			    test_context.raiseEvent(TestErrorEventType.create("Unable to install plugin " + plugin.getClass().getSimpleName() + " due to " + e.getMessage()));
			    }
	    }

    protected MuseProject _project;
    private String _output_path = null;
    @SuppressWarnings("WeakerAccess")  // available to subclasses
    protected TestSuiteOutputOnDisk _output = null;
    private TestSuiteExecutionContext _context;

    private final static Logger LOG = LoggerFactory.getLogger(SimpleTestSuiteRunner.class);
    }