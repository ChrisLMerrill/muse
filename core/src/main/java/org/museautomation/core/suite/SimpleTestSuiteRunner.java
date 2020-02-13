package org.museautomation.core.suite;

import org.jetbrains.annotations.*;
import org.museautomation.core.*;
import org.museautomation.core.context.*;
import org.museautomation.core.events.*;
import org.museautomation.core.execution.*;
import org.museautomation.core.plugins.*;
import org.museautomation.core.test.*;

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
        _context = new DefaultTestSuiteExecutionContext(new ProjectExecutionContext(project), suite);

        Plugins.setup(_context);
        for (MusePlugin plugin : manual_plugins)
        	_context.addPlugin(plugin);

        try
	        {
	        _context.initializePlugins();
	        }
        catch (MuseExecutionError e)
	        {
	        final String message = "Unable to initialize test suite plugins due to: " + e.getMessage();
	        System.err.println(message);
	        _context.raiseEvent(TestErrorEventType.create(message));
	        return false;
	        }

        _context.raiseEvent(StartSuiteEventType.create(suite));
        boolean suite_success = runTests(suite);
        _context.raiseEvent(EndSuiteEventType.create(suite));

        _context.cleanup();

        return suite_success;
        }

    @SuppressWarnings("WeakerAccess")  // intended to be overridden by implementations with more complex methods (e.g. parallel)
    protected boolean runTests(MuseTestSuite suite)
	    {
	    boolean suite_success = true;
	    final Iterator<TestConfiguration> tests = suite.getTests(_project);
	    while (tests.hasNext())
		    {
		    final boolean test_success = runTest(tests.next());
		    if (!test_success)
			    suite_success = false;
		    }
	    return suite_success;
	    }

    @SuppressWarnings("WeakerAccess")  // external API
    protected boolean runTest(TestConfiguration configuration)
        {
        SimpleTestRunner runner = createRunner(configuration);
        MuseEvent start_event = raiseTestStartEvent(configuration);
        runner.runTest();
        raiseTestEndEvent(start_event);
        return runner.completedNormally();
        }

    @Override
    public void setOutputPath(String path)
	    {
	    }

    @SuppressWarnings("WeakerAccess")  // extensions may override
    protected MuseEvent raiseTestStartEvent(TestConfiguration configuration)
	    {
	    final MuseEvent start_event = StartSuiteTestEventType.create(_context.createVariable("testconfig", configuration));
	    _context.raiseEvent(start_event);
	    return start_event;
	    }

    @SuppressWarnings("WeakerAccess")  // extensions may override
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
	    return new SimpleTestRunner(_context, configuration);
	    }

    protected MuseProject _project;
    @SuppressWarnings("WeakerAccess")  // available to subclasses
    protected TestSuiteExecutionContext _context;
    }