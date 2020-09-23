package org.museautomation.core.task;

import org.museautomation.core.*;
import org.museautomation.core.context.*;
import org.museautomation.core.plugins.*;

import java.util.*;

/**
 * Everything needed to run a test.
 *
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class BasicTaskConfiguration implements TaskConfiguration
    {
    public BasicTaskConfiguration(String task_id)
        {
        _task_id = task_id;
        }

    public BasicTaskConfiguration(MuseTask task)
	    {
	    _task = task;
	    }

    public MuseTask task()
        {
        if (_task == null && _parent_context != null)
	        {
	        _task = _parent_context.getProject().getResourceStorage().getResource(_task_id, MuseTask.class);
	        if (_task == null)  // not found in project
	        	_task = new MissingTask(_task_id);
	        }
        return _task;
        }

    public String name()
        {
        if (_name != null)
            return _name;
        if (_task_id != null)
            return _task_id;
        return task().getId();
        }

    @Override
    public void withinContext(MuseExecutionContext parent_context)
	    {
	    if (_parent_context == null)
	        _parent_context = parent_context;
	    else if (!_parent_context.equals(parent_context))
	    	throw new IllegalArgumentException("Cannot use a config within a 2nd context");
	    }

    public TaskExecutionContext context()
	    {
	    if (_context == null)
	    	_context = TaskExecutionContextFactory.create(_parent_context, task());
	    return _context;
	    }

    public String getName()
	    {
	    return name();
	    }
    public void setName(String name)
        {
        _name = name;
        }

    public List<MusePlugin> plugins()
        {
        if (_plugins == null)
        	return Collections.emptyList();
        return Collections.unmodifiableList(_plugins);
        }

    public void addPlugin(MusePlugin plugin)
	    {
    	if (_plugins == null)
    		_plugins = new ArrayList<>();
    	_plugins.add(plugin);
        }

    @Override
    public String toString()
        {
        return getName();
        }

    // these are configurations that would be persisted
    private String _task_id;
    private String _name;
    private List<MusePlugin> _plugins;

    // these are set/cached for use at test execution time
    private transient MuseExecutionContext _parent_context;
    private MuseTask _task;
    private TaskExecutionContext _context;
    }