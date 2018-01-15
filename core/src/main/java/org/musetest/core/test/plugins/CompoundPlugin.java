package org.musetest.core.test.plugins;

import org.musetest.core.*;
import org.musetest.core.resource.*;
import org.musetest.core.test.plugin.*;
import org.musetest.core.values.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class CompoundPlugin extends BaseTestPlugin
	{
	public CompoundPlugin(CompoundPluginConfiguration config)
		{
		super(config);
		}

	@Override
	public String getType()
		{
		return CompoundPluginConfiguration.TYPE_ID;
		}

	@Override
	public boolean addToContext(MuseExecutionContext context, boolean automatic) throws MuseExecutionError
		{
		if (automatic && !applyAutomatically(context))
			return false;

		if (!applyToThisTest(context))
			return false;

		MuseValueSource source = BaseValueSource.getValueSource(_configuration.parameters(), CompoundPluginConfiguration.LISTS_PARAM, true, context.getProject());
		String id_list_param = BaseValueSource.getValue(source, context, false, String.class);
		String[] id_list = id_list_param.split(",");

		for (String id : id_list)
			{
			final ResourceToken resource = context.getProject().getResourceStorage().findResource(id);
			if (resource.getResource() instanceof TestPluginConfiguration)
				((TestPluginConfiguration)resource.getResource()).createPlugin().addToContext(context, false);
			}
		return true;
		}

	@Override
	public void initialize(MuseExecutionContext context)
		{
		}
	}
