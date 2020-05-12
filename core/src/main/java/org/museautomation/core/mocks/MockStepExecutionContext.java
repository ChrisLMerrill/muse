package org.museautomation.core.mocks;

import org.museautomation.core.*;
import org.museautomation.core.context.*;
import org.museautomation.core.events.*;
import org.museautomation.core.output.*;
import org.museautomation.core.plugins.*;
import org.museautomation.core.project.*;
import org.museautomation.core.step.*;
import org.museautomation.core.steptask.*;
import org.museautomation.core.task.*;
import org.museautomation.core.variables.*;

import java.util.*;

/**
 * An implementation of StepExecutionContext that also instantiates it's own TestExecutionContext
 * and MuseProject, if needed, for purposes of unit testing.
 *
 * It supports some functionality, such as event listeners, raising events and getting/setting variables
 * in a minimal implementation. Other methods are stubbed as NOOPs.
 *
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class MockStepExecutionContext implements StepExecutionContext
    {
    public MockStepExecutionContext()
        {
        this(new SimpleProject());
        }

	public MockStepExecutionContext(SteppedTask task)
		{
		this(new SimpleProject(), task);
		}

    public MockStepExecutionContext(MuseProject project)
        {
        this(project, new SteppedTask(new StepConfiguration("mock-step")));
        }

	public MockStepExecutionContext(MuseProject project, SteppedTask task)
		{
		_test_context = new DefaultSteppedTaskExecutionContext(project, task);
//		_test_context.addPlugin(new EventLogWriter());
		}

    public MockStepExecutionContext(SteppedTaskExecutionContext task_context)
        {
//		test_context.addPlugin(new EventLogger());
        _test_context = task_context;
        }

    @Override
    public StepConfiguration getCurrentStepConfiguration()
        {
        return null;
        }

    @Override
    public MuseStep getCurrentStep()
        {
        return null;
        }

    @Override
    public void stepComplete(MuseStep step, StepExecutionResult result)
        {
        }

    @Override
    public boolean hasStepToExecute()
        {
        return false;
        }

    @Override
    public Object getVariable(String name)
        {
        Object val = _variables.get(name);
        if (val == null && _test_context != null)
            val = _test_context.getVariable(name);
        return val;
        }

    @Override
    public void setVariable(String name, Object value)
        {
        if (_test_context == null)
            _variables.put(name, value);
        else
            _test_context.setVariable(name, value);
        }

    @Override
    public Object getVariable(String name, VariableQueryScope scope)
        {
        return _variables.get(name);
        }

    @Override
    public void setVariable(String name, Object value, ContextVariableScope scope)
        {
        _variables.put(name, value);
        }

    @Override
    public String createVariable(String prefix, Object value)
	    {
	    return null;
	    }

    @Override
    public void raiseEvent(MuseEvent event)
        {
        _test_context.raiseEvent(event);
        }

    @Override
    public MuseProject getProject()
        {
        return _test_context.getProject();
        }

    @Override
    public StepExecutionContextStack getExecutionStack()
        {
        return _test_context.getExecutionStack();
        }

    @Override
    public void registerShuttable(Shuttable shuttable)
        {
        _test_context.registerShuttable(shuttable);
        }

    @Override
    public SteppedTaskExecutionContext getParent()
        {
        return _test_context;
        }

    @Override
    public void addEventListener(MuseEventListener listener)
        {
        _test_context.addEventListener(listener);
        }

    @Override
    public void removeEventListener(MuseEventListener listener)
        {
        _test_context.removeEventListener(listener);
        }

    @Override
    public EventLog getEventLog()
	    {
	    return _test_context.getEventLog();
	    }

    @Override
    public void cleanup() {}

    @Override
    public void addPlugin(MusePlugin plugin)
		{
	    _test_context.addPlugin(plugin);
		}

    @Override
    public List<MusePlugin> getPlugins()
	    {
	    return Collections.emptyList();
	    }

    @Override
    public int initializePlugins() throws MuseExecutionError
		{
		return _test_context.initializePlugins();
		}

    @Override
    public StepLocator getStepLocator()
	    {
	    if (_locator == null)
		    {
		    if (_test_context != null)
		    	_locator = _test_context.getStepLocator();
		    else
			    _locator = new CachedLookupStepLocator();
		    }
	    return _locator;
	    }

    @Override
    public ExecutionOutputs outputs()
        {
        return _outputs;
        }

    @Override
    public ContextVariableScope getVariableScope()
	    {
	    return ContextVariableScope.Execution;
	    }

    private final SteppedTaskExecutionContext _test_context;
    private final Map<String, Object> _variables = new HashMap<>();
    private StepLocator _locator;
    private final ExecutionOutputs _outputs = new ExecutionOutputs();
    }
