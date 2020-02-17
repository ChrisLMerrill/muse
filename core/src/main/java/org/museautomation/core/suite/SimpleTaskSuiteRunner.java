package org.museautomation.core.suite;

import org.jetbrains.annotations.*;
import org.museautomation.core.*;
import org.museautomation.core.context.*;
import org.museautomation.core.events.*;
import org.museautomation.core.execution.*;
import org.museautomation.core.plugins.*;
import org.museautomation.core.task.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class SimpleTaskSuiteRunner implements MuseTaskSuiteRunner
    {
    @Override
    public boolean execute(MuseProject project, MuseTaskSuite suite, List<MusePlugin> manual_plugins)
        {
        _project = project;
        _context = new DefaultTaskSuiteExecutionContext(new ProjectExecutionContext(project), suite);

        Plugins.setup(_context);
        for (MusePlugin plugin : manual_plugins)
        	_context.addPlugin(plugin);

        try
	        {
	        _context.initializePlugins();
	        }
        catch (MuseExecutionError e)
	        {
	        final String message = "Unable to initialize task suite plugins due to: " + e.getMessage();
	        System.err.println(message);
	        _context.raiseEvent(TaskErrorEventType.create(message));
	        return false;
	        }

        _context.raiseEvent(StartSuiteEventType.create(suite));
        boolean suite_success = runTasks(suite);
        _context.raiseEvent(EndSuiteEventType.create(suite));

        _context.cleanup();

        return suite_success;
        }

    @SuppressWarnings("WeakerAccess")  // intended to be overridden by implementations with more complex methods (e.g. parallel)
    protected boolean runTasks(MuseTaskSuite suite)
	    {
	    boolean suite_success = true;
	    final Iterator<TaskConfiguration> tasks = suite.getTasks(_project);
	    while (tasks.hasNext())
		    {
		    final boolean task_success = runTask(tasks.next());
		    if (!task_success)
			    suite_success = false;
		    }
	    return suite_success;
	    }

    @SuppressWarnings("WeakerAccess")  // external API
    protected boolean runTask(TaskConfiguration configuration)
        {
        SimpleTaskRunner runner = createRunner(configuration);
        MuseEvent start_event = raiseTaskStartEvent(configuration);
        runner.runTask();
        raiseTaskEndEvent(start_event);
        return runner.completedNormally();
        }

    @Override
    public void setOutputPath(String path)
	    {
	    }

    @SuppressWarnings("WeakerAccess")  // extensions may override
    protected MuseEvent raiseTaskStartEvent(TaskConfiguration configuration)
	    {
	    final MuseEvent start_event = StartSuiteTaskEventType.create(_context.createVariable("taskconfig", configuration));
	    _context.raiseEvent(start_event);
	    return start_event;
	    }

    @SuppressWarnings("WeakerAccess")  // extensions may override
	protected void raiseTaskEndEvent(MuseEvent start_event)
		{
		final String config_var_name = StartSuiteTaskEventType.getConfigVariableName(start_event);
		final MuseEvent end_event = EndSuiteTaskEventType.create(config_var_name);
		_context.raiseEvent(end_event);
		_context.setVariable(config_var_name, null);
		}

    @NotNull
    @SuppressWarnings("WeakerAccess")  // overridden in GUI.
    protected SimpleTaskRunner createRunner(TaskConfiguration configuration)
	    {
	    return new SimpleTaskRunner(_context, configuration);
	    }

    protected MuseProject _project;
    protected TaskSuiteExecutionContext _context;
    }