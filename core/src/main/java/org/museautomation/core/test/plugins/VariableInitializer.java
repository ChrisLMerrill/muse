package org.museautomation.core.test.plugins;

import org.museautomation.core.*;
import org.museautomation.core.context.*;
import org.museautomation.core.plugins.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class VariableInitializer implements MusePlugin
	{
	public VariableInitializer(String name, Object value)
		{
		_variables.put(name, value);
		}

	@Override
	public void initialize(MuseExecutionContext context)
		{
		for (String name : _variables.keySet())
			context.setVariable(name, _variables.get(name), _scope);
		}

	@Override
	public boolean conditionallyAddToContext(MuseExecutionContext context, boolean automatic)
		{
		context.addPlugin(this);
		return true;
		}

	@Override
	public String getId()
		{
		return "no/id";
		}

	@Override
	public void shutdown()
		{

		}

	private Map<String, Object> _variables = new HashMap<>();
	private ContextVariableScope _scope = ContextVariableScope.Execution;
	}
