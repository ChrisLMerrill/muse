package org.musetest.core.test.plugins;

import org.musetest.core.*;
import org.musetest.core.values.*;
import org.musetest.core.variables.*;

import javax.annotation.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class VariableListInitializer implements TestPlugin
	{
	@Override
	public String getType()
		{
		return TYPE_ID;
		}

	@Override
	public void configure(@Nonnull TestPluginConfiguration configuration)
		{
		_configuration = configuration;
		}

	@Override
	public void initialize(MuseExecutionContext context) throws MuseExecutionError
		{
		// put the list name into the context (for evaluation by value sources if needed)
		String list_id = BaseValueSource.getValue(_configuration.getParameters().get(LIST_ID_PARAM).createSource(context.getProject()), context, false, String.class);
		context.setVariable(ProjectVarsInitializerSysvarProvider.VARIABLE_LIST_ID_VARNAME, list_id);

		// evaluate the include condition to determine if the list in this initializer should be included
		ValueSourceConfiguration condition_config = _configuration.getApplyCondition();
		MuseValueSource condition_source = condition_config.createSource(context.getProject());
		Object condition = condition_source.resolveValue(context);
		Boolean set_list;
		if (condition instanceof Boolean)
			set_list = (Boolean) condition;
		else
			throw new IllegalArgumentException("The condition source of a VariableListContextInitializerConfiguration must resolve to a boolean value. The source (" + condition_source + ") resolved to " + condition);

		if (set_list)
			{
			// set the variables in the list into the context
			VariableList list = context.getProject().getResourceStorage().getResource(list_id, VariableList.class);
			for (String name : list.getVariables().keySet())
				{
				ValueSourceConfiguration config = list.getVariables().get(name);
				Object value = config.createSource(context.getProject()).resolveValue(context);
				context.setVariable(name, value, VariableScope.Execution);
				}
			}

		context.setVariable(ProjectVarsInitializerSysvarProvider.VARIABLE_LIST_ID_VARNAME,null);
		}

	private TestPluginConfiguration _configuration;

	public final static String TYPE_ID = "varlist";
	public final static String LIST_ID_PARAM = "listid";

	@SuppressWarnings("unused") // used by reflection
	public static class VariableListType extends TestPluginType
		{
		@Override
		public String getTypeId()
			{
			return TYPE_ID;
			}

		@Override
		public String getDisplayName()
			{
			return "Variable List";
			}

		@Override
		public String getShortDescription()
			{
			return "Copy variables from a list into the context";
			}
		}
	}
