package org.musetest.core.context.initializers;

import org.musetest.core.*;
import org.musetest.core.context.*;

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
    public void initialize(MuseProject project, MuseExecutionContext context) throws MuseExecutionError
        {
        for (String name : _variables.keySet())
            context.setVariable(name, _variables.get(name));
        }

    private final Map<String, Object> _variables;
    }


