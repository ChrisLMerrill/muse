package org.museautomation.core.task.plugins;

import org.museautomation.core.*;
import org.museautomation.core.context.*;
import org.museautomation.core.plugins.*;
import org.museautomation.core.resource.generic.*;
import org.museautomation.core.values.*;
import org.museautomation.core.values.factory.*;
import org.museautomation.core.variables.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class TaskDefaultsInitializer extends GenericConfigurableTaskPlugin
    {
    TaskDefaultsInitializer(GenericResourceConfiguration configuration)
	    {
	    super(configuration);
	    }

    @Override
    public void initialize(MuseExecutionContext context) throws MuseExecutionError
        {
        boolean overwrite = true;
        MuseValueSource overwrite_source = BaseValueSource.getValueSource(_configuration.parameters(), TaskDefaultsInitializerConfiguration.OVERWRITE_PARAM, false, context.getProject());
        if (overwrite_source != null)
	        overwrite = BaseValueSource.getValue(overwrite_source, context, false, Boolean.class);

        final TaskExecutionContext test_context = findTestContext(context);
        Map<String, ValueSourceConfiguration> defaults = test_context.getTask().getDefaultVariables();
        if (defaults != null)
            for (String name : defaults.keySet())
                if (overwrite || context.getVariable(name, VariableQueryScope.Any) == null)
                    {
                    MuseValueSource source = ValueSourceFactory.getDefault(context.getProject()).createSource(defaults.get(name), context.getProject());
                    Object value = source.resolveValue(context);
                    context.setVariable(name, value, ContextVariableScope.Execution);
                    }
        }

    private TaskExecutionContext findTestContext(MuseExecutionContext context)
	    {
	    while (context != null)
		    {
		    if (context instanceof TaskExecutionContext)
		    	return (TaskExecutionContext) context;
		    context = context.getParent();
		    }
	    return null;
	    }
    }
