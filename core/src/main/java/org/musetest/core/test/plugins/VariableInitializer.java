package org.musetest.core.test.plugins;

import org.musetest.core.*;
import org.musetest.core.plugins.*;
import org.musetest.core.variables.*;

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
	public void conditionallyAddToContext(MuseExecutionContext context, boolean automatic)
		{
		context.addPlugin(this);
		}

	private Map<String, Object> _variables = new HashMap<>();
	private VariableScope _scope = VariableScope.Execution;
	}
