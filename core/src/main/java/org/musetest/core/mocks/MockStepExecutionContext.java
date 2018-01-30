package org.musetest.core.mocks;

import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.datacollection.*;
import org.musetest.core.events.*;
import org.musetest.core.project.*;
import org.musetest.core.step.*;
import org.musetest.core.steptest.*;
import org.musetest.core.test.*;
import org.musetest.core.test.plugin.*;
import org.musetest.core.variables.*;

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

	public MockStepExecutionContext(SteppedTest test)
		{
		this(new SimpleProject(), test);
		}

    public MockStepExecutionContext(MuseProject project)
        {
        this(project, new SteppedTest(new StepConfiguration("mock-step")));
        }

	public MockStepExecutionContext(MuseProject project, SteppedTest test)
		{
		_test_context = new DefaultSteppedTestExecutionContext(project, test);
		_test_context.addTestPlugin(new EventLogger());
		}

    public MockStepExecutionContext(SteppedTestExecutionContext test_context)
        {
		test_context.addTestPlugin(new EventLogger());
        _test_context = test_context;
        }

    final SteppedTestExecutionContext _test_context;

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
    public Map<String, Object> getVariables()
        {
        return _variables;
        }

    @Override
    public Object getVariable(String name)
        {
        return _variables.get(name);
        }

    @Override
    public void setVariable(String name, Object value)
        {
        _variables.put(name, value);
        }

    @Override
    public Object getVariable(String name, VariableScope scope)
        {
        return _variables.get(name);
        }

    @Override
    public void setVariable(String name, Object value, VariableScope scope)
        {
        _variables.put(name, value);
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
    public SteppedTestExecutionContext getParent()
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
    public void cleanup() {}

    @Override
    public void addTestPlugin(TestPlugin plugin)
		{
	    _test_context.addTestPlugin(plugin);
		}

    @Override
    public void initializePlugins(MuseExecutionContext context) throws MuseExecutionError
		{
		_test_context.initializePlugins(null);
		}

	@Override
	public List<DataCollector> getDataCollectors()
		{
		return _test_context.getDataCollectors();
		}

	@Override
	public <T extends DataCollector> T getDataCollector(Class<T> type)
		{
		return _test_context.getDataCollector(type);
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

    private Map<String, Object> _variables = new HashMap<>();
    private StepLocator _locator;
    }
