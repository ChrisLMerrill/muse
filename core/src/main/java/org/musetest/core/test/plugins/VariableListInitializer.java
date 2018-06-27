package org.musetest.core.test.plugins;

import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.events.*;
import org.musetest.core.plugins.*;
import org.musetest.core.values.*;
import org.musetest.core.variables.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class VariableListInitializer extends GenericConfigurablePlugin
	{
	VariableListInitializer(VariableListInitializerConfiguration configuration)
		{
		super(configuration);
		}

	@Override
	public void initialize(MuseExecutionContext context) throws MuseExecutionError
		{
		// find the list
		String list_id = BaseValueSource.getValue(_configuration.getParameters().get(LIST_ID_PARAM).createSource(context.getProject()), context, false, String.class);
		VariableList list = context.getProject().getResourceStorage().getResource(list_id, VariableList.class);
		if (list == null)
			{
			context.raiseEvent(TestErrorEventType.create(String.format("Unable to initialize variables from list '%s' because it was not found in the project.", list_id)));
			return;
			}

		// set the variables in the list into the context
		context.setVariable(ProjectVarsInitializerSysvarProvider.VARIABLE_LIST_ID_VARNAME, list_id);
		for (String name : list.getVariables().keySet())
			{
			ValueSourceConfiguration config = list.getVariables().get(name);
			Object value = config.createSource(context.getProject()).resolveValue(context);
			context.setVariable(name, value, ContextVariableScope.Execution);
			}
		context.setVariable(ProjectVarsInitializerSysvarProvider.VARIABLE_LIST_ID_VARNAME,null);

		}

	@Override
	protected boolean applyToContextType(MuseExecutionContext context)
		{
		return true;
		}

	public final static String TYPE_ID = "varlist";
	private final static String LIST_ID_PARAM = "listid";
	}
