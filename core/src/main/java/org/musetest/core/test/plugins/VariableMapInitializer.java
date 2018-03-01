package org.musetest.core.test.plugins;

import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.plugins.*;

import java.util.*;

/**
 * Inject a map into the context. Intended for use by the ParameterizedTestSuite.
 *
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class VariableMapInitializer implements MusePlugin
    {
    public VariableMapInitializer(Map<String, Object> variables)
        {
        _variables = variables;
        }

    @Override
    public boolean conditionallyAddToContext(MuseExecutionContext context, boolean automatic)
	    {
	    context.addPlugin(this);
	    return true;
	    }

    @Override
    public void initialize(MuseExecutionContext context)
        {
        for (String name : _variables.keySet())
            context.setVariable(name, _variables.get(name), ContextVariableScope.Execution);
        }

    private final Map<String, Object> _variables;
    }
