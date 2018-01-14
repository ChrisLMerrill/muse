package org.musetest.core.test.plugins;

import org.musetest.core.*;
import org.musetest.core.test.plugin.*;
import org.musetest.core.variables.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class VariableInitializer implements TestPlugin
	{
	public VariableInitializer(String name, Object value)
		{
		_variables.put(name, value);
		}

	@Override
	public String getType()
		{
		return null;
		}

	@Override
	public void initialize(MuseExecutionContext context)
		{
		for (String name : _variables.keySet())
			context.setVariable(name, _variables.get(name), _scope);
		}

	@Override
	public boolean applyAutomatically(MuseExecutionContext context)
		{
		return true;
		}

	@Override
	public boolean applyToThisTest(MuseExecutionContext context)
		{
		return true;
		}

	private Map<String, Object> _variables = new HashMap<>();
	private VariableScope _scope = VariableScope.Execution;
	}
