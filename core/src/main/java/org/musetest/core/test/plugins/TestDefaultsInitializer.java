package org.musetest.core.test.plugins;

import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.test.plugin.*;
import org.musetest.core.values.*;
import org.musetest.core.values.factory.*;
import org.musetest.core.variables.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class TestDefaultsInitializer implements TestPlugin
    {
    @Override
    public String getType()
	    {
	    return "test-defaults";
	    }

    @Override
    public boolean addToContext(MuseExecutionContext context, boolean automatic)
	    {
	    context.addTestPlugin(this);
	    return true;
	    }

    @Override
    public void initialize(MuseExecutionContext context) throws MuseExecutionError
        {
        final TestExecutionContext test_context = findTestContext(context);
        Map<String, ValueSourceConfiguration> defaults = test_context.getTest().getDefaultVariables();
        if (defaults != null)
            for (String name : defaults.keySet())
                if (context.getVariable(name, VariableScope.Execution) == null)
                    {
                    MuseValueSource source = ValueSourceFactory.getDefault(context.getProject()).createSource(defaults.get(name), context.getProject());
                    Object value = source.resolveValue(context);
                    context.setVariable(name, value, VariableScope.Execution);
                    }
        }

    private TestExecutionContext findTestContext(MuseExecutionContext context)
	    {
	    while (context != null)
		    {
		    if (context instanceof TestExecutionContext)
		    	return (TestExecutionContext) context;
		    context = context.getParent();
		    }
	    return null;
	    }
    }


