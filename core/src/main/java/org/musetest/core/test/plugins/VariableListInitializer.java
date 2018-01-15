package org.musetest.core.test.plugins;

import org.musetest.core.*;
import org.musetest.core.test.plugin.*;
import org.musetest.core.values.*;
import org.musetest.core.variables.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class VariableListInitializer extends BaseTestPlugin
	{
	VariableListInitializer(VariableListInitializerConfiguration configuration)
		{
		super(configuration);
		}

	@Override
	public String getType()
		{
		return TYPE_ID;
		}

	@Override
	public void initialize(MuseExecutionContext context) throws MuseExecutionError
		{
		// put the list name into the context (for evaluation by value sources if needed)
		String list_id = BaseValueSource.getValue(_configuration.getParameters().get(LIST_ID_PARAM).createSource(context.getProject()), context, false, String.class);
		context.setVariable(ProjectVarsInitializerSysvarProvider.VARIABLE_LIST_ID_VARNAME, list_id);

		// set the variables in the list into the context
		VariableList list = context.getProject().getResourceStorage().getResource(list_id, VariableList.class);
		for (String name : list.getVariables().keySet())
			{
			ValueSourceConfiguration config = list.getVariables().get(name);
			Object value = config.createSource(context.getProject()).resolveValue(context);
			context.setVariable(name, value, VariableScope.Execution);
			}

		context.setVariable(ProjectVarsInitializerSysvarProvider.VARIABLE_LIST_ID_VARNAME,null);
		}

	public final static String TYPE_ID = "varlist";
	public final static String LIST_ID_PARAM = "listid";
	}
