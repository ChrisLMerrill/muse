package org.musetest.core.context.initializers;

import org.musetest.core.*;
import org.musetest.core.context.*;

import javax.annotation.*;
import java.util.*;

/**
 * Inject a map into the context. Intended for use by the ParameterizedTestSuite.
 *
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class VariableMapInitializer implements ContextInitializer
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
    public void configure(@Nonnull ContextInitializerConfiguration configuration)
	    {
	    // doesn't currently need configuration
	    }

    @Override
    public void initialize(MuseExecutionContext context) throws MuseExecutionError
        {
        for (String name : _variables.keySet())
            context.setVariable(name, _variables.get(name));
        }

    private final Map<String, Object> _variables;
    }


