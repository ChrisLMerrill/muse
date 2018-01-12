package org.musetest.core.test.plugins;

import org.musetest.core.*;
import org.musetest.core.variables.*;

import javax.annotation.*;
import java.util.*;

/**
 * Inject a map into the context. Intended for use by the ParameterizedTestSuite.
 *
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class VariableMapInitializer implements TestPlugin
    {
    public VariableMapInitializer(Map<String, Object> variables)
        {
        _variables = variables;
        }

    @Override
    public String getType()
	    {
	    return "variable-map";
	    }

    @Override
    public void configure(@Nonnull TestPluginConfiguration configuration)
	    {
	    // doesn't currently need configuration
	    }

    @Override
    public void initialize(MuseExecutionContext context)
        {
        for (String name : _variables.keySet())
            context.setVariable(name, _variables.get(name), VariableScope.Execution);
        }

    private final Map<String, Object> _variables;
    }
