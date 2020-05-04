package org.museautomation.core.execution;

import org.museautomation.core.*;
import org.museautomation.core.context.*;
import org.museautomation.core.events.*;
import org.museautomation.core.plugins.*;
import org.museautomation.core.task.*;
import org.slf4j.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@SuppressWarnings("WeakerAccess")  // part of public API
public class SimpleTaskRunner implements TaskRunner
    {
    public SimpleTaskRunner(MuseExecutionContext context, TaskConfiguration config)
	    {
	    _context = context;
	    _config = config;
        _config.withinContext(_context);  // it is important to initialize this context immediately, so that the context is available for suite events before the task starts (i.e. can subscribe to events immediately)
	    }

    public SimpleTaskRunner(TaskExecutionContext context)
        {
        _config = new TaskHolder(context.getTask(), context);
        }

    @Override
    public void runTask()
        {
        startTask();
        _completed_normally = _config.task().execute(_config.context());
        finishTask();
        }

    @Override
    public Boolean completedNormally()
	    {
	    return _completed_normally;
	    }

    protected void startTask()
	    {
	    _task = _config.task();
	    _task_context = _config.context();
	    try
		    {
		    for (MusePlugin plugin : _config.plugins())
			    _task_context.addPlugin(plugin);
		    for (MusePlugin plugin : getAdditionalPlugins(_config))
                _task_context.addPlugin(plugin);
		    Plugins.setup(_task_context);
		    _task_context.initializePlugins();
		    }
	    catch (MuseExecutionError e)
		    {
		    final String message = "Unable to setup the task plugins due to: " + e.getMessage();
		    LOG.error(message);
		    this._task_context.raiseEvent(TaskErrorEventType.create(message));
		    }
	    _task_context.raiseEvent(StartTaskEventType.create(_task.getId(), _config.name()));
	    }

    protected void finishTask()
	    {
	    _task_context.raiseEvent(EndTaskEventType.create());
	    try
	        {
	        _config.context().cleanup();
	        }
	    catch (Throwable e)
	        {
	        LOG.error("Exception during task cleanup:", e);
	        // regardless of exception here, we want to return normally.
	        }
	    }

    @Override
    public TaskExecutionContext getExecutionContext()
        {
        return _config.context();
        }

    protected List<MusePlugin> getAdditionalPlugins(@SuppressWarnings("unused") TaskConfiguration config)
        {
        return Collections.emptyList();
        }

    private final TaskConfiguration _config;
    protected MuseExecutionContext _context;
    protected TaskExecutionContext _task_context;
    protected MuseTask _task;
    protected Boolean _completed_normally;

    private static class TaskHolder implements TaskConfiguration
	    {
	    public TaskHolder(MuseTask task, TaskExecutionContext context)
		    {
		    _task = task;
		    _context = context;
		    }

	    @Override
	    public void withinContext(MuseExecutionContext parent_context)
		    {

		    }

	    @Override
	    public MuseTask task()
		    {
		    return _task;
		    }

	    @Override
	    public String name()
		    {
		    return _task.getDescription();
		    }

	    @Override
	    public TaskExecutionContext context()
		    {
		    return _context;
		    }

	    @Override
	    public void addPlugin(MusePlugin plugin)
		    {
		    }

	    @Override
	    public List<MusePlugin> plugins()
		    {
		    return Collections.emptyList();
		    }

	    MuseTask _task;
	    TaskExecutionContext _context;
	    }

    private final static Logger LOG = LoggerFactory.getLogger(SimpleTaskRunner.class);
    }
