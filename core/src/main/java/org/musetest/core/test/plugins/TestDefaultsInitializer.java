package org.musetest.core.test.plugins;

import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.values.*;
import org.musetest.core.values.factory.*;
import org.musetest.core.variables.*;

import javax.annotation.*;
import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class TestDefaultsInitializer implements TestPlugin
    {
    public TestDefaultsInitializer(TestExecutionContext test_context)
        {
        _test_context = test_context;
        }

    @Override
    public String getType()
	    {
	    return "test-defaults";
	    }

    @Override
    public void configure(@Nonnull TestPluginConfiguration configuration)
	    {
	    // doesn't currently need configuration
	    }

    @Override
    public void initialize(MuseExecutionContext context) throws MuseExecutionError
        {
        Map<String, ValueSourceConfiguration> defaults = _test_context.getTest().getDefaultVariables();
        if (defaults != null)
            for (String name : defaults.keySet())
                if (context.getVariable(name, VariableScope.Execution) == null)
                    {
                    MuseValueSource source = ValueSourceFactory.getDefault(_test_context.getProject()).createSource(defaults.get(name), _test_context.getProject());
                    Object value = source.resolveValue(context);
                    context.setVariable(name, value, VariableScope.Execution);
                    }
        }

    private TestExecutionContext _test_context;
    }

