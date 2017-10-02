package org.musetest.core.context;

import org.musetest.core.*;
import org.musetest.core.datacollection.*;
import org.musetest.core.resource.*;
import org.musetest.core.step.*;
import org.musetest.core.steptest.*;
import org.musetest.core.test.*;
import org.musetest.core.variables.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
abstract class BaseStepExecutionContext implements StepExecutionContext
	{
	BaseStepExecutionContext(StepsExecutionContext parent_context, boolean new_variable_scope)
		{
		_parent_context = parent_context;
		if (new_variable_scope)
			_step_vars = new HashMap<>();
		}

	@Override
	public StepConfiguration getCurrentStepConfiguration()
		{
		return null;
		}

	@Override
	public MuseStep getCurrentStep() throws MuseInstantiationException
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
		return _parent_context.getVariable(name);
		}

	@Override
	public void setVariable(String name, Object value)
		{
		_parent_context.setVariable(name, value);
		}

	@Override
	public Object getVariable(String name, VariableScope scope)
		{
		return _parent_context.getVariable(name, scope);
		}

	@Override
	public void setVariable(String name, Object value, VariableScope scope)
		{
		_parent_context.setVariable(name, value, scope);
		}

	@Override
	public Map<String, Object> getVariables()
		{
		return _step_vars;
		}

	@Override
	public StepExecutionContextStack getExecutionStack()
		{
		return _parent_context.getExecutionStack();
		}

	@Override
	public void raiseEvent(MuseEvent event)
		{
		_parent_context.raiseEvent(event);
		}

	@Override
	public MuseProject getProject()
		{
		return _parent_context.getProject();
		}

	@Override
	public void registerShuttable(Shuttable shuttable)
		{
		_parent_context.registerShuttable(shuttable);
		}

	@Override
	public StepsExecutionContext getParent()
		{
		return _parent_context;
		}

	@Override
	public void removeEventListener(MuseEventListener listener)
		{
		_parent_context.removeEventListener(listener);
		}

	@Override
	public void addEventListener(MuseEventListener listener)
		{
		_parent_context.addEventListener(listener);
		}

	@Override
	public void cleanup()
		{
		_parent_context.cleanup();
		}

	@Override
	public void addInitializer(ContextInitializer initializer)
		{
		_parent_context.addInitializer(initializer);
		}

	@Override
	public void runInitializers() throws MuseExecutionError
		{
		_parent_context.runInitializers();
		}

	@Override
	public List<DataCollector> getDataCollectors()
		{
		return _parent_context.getDataCollectors();
		}

	private StepsExecutionContext _parent_context;
	private Map<String, Object> _step_vars = null;
	}


