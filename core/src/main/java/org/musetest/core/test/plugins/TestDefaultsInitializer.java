package org.musetest.core.test.plugins;

import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.plugins.*;
import org.musetest.core.resource.generic.*;
import org.musetest.core.values.*;
import org.musetest.core.values.factory.*;
import org.musetest.core.variables.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class TestDefaultsInitializer extends GenericConfigurableTestPlugin
    {
    TestDefaultsInitializer(GenericResourceConfiguration configuration)
	    {
	    super(configuration);
	    }

    @Override
    public void initialize(MuseExecutionContext context) throws MuseExecutionError
        {
        boolean overwrite = true;
        MuseValueSource overwrite_source = BaseValueSource.getValueSource(_configuration.parameters(), AUTO_APPLY_PARAM, false, context.getProject());
        if (overwrite_source != null)
	        overwrite = BaseValueSource.getValue(overwrite_source, context, false, Boolean.class);

        final TestExecutionContext test_context = findTestContext(context);
        Map<String, ValueSourceConfiguration> defaults = test_context.getTest().getDefaultVariables();
        if (defaults != null)
            for (String name : defaults.keySet())
                if (overwrite || context.getVariable(name, VariableScope.Execution) == null)
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


