package org.musetest.core.test.plugins;

import org.musetest.core.*;
import org.musetest.core.events.*;
import org.musetest.core.plugins.*;
import org.musetest.core.resource.*;
import org.musetest.core.values.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class CompoundPlugin extends GenericConfigurablePlugin
	{
	CompoundPlugin(CompoundPluginConfiguration config)
		{
		super(config);
		}

	@Override
	public boolean conditionallyAddToContext(MuseExecutionContext context, boolean automatic) throws MuseExecutionError
		{
		if (automatic && !applyAutomatically(context))
			return false;

		if (!applyToThisTest(context))
			return false;

		MuseValueSource source = BaseValueSource.getValueSource(_configuration.parameters(), CompoundPluginConfiguration.LISTS_PARAM, true, context.getProject());
		Object id_list_obj = BaseValueSource.getValue(source, context, false);
		String[] id_list;
		if (id_list_obj instanceof List)
			{
			final List<String> list = (List) id_list_obj;
			id_list = new String[list.size()];
			for (int i = 0; i < list.size(); i++)
				id_list[i] = ((List) id_list_obj).get(i).toString();
			}
		else if (id_list_obj.toString().contains(","))
			{
			String id_list_param = BaseValueSource.getValue(source, context, false, String.class);
			id_list = id_list_param.split(",");
			}
		else
			{
			id_list = new String[1];
			id_list[0] = id_list_obj.toString();
			}

		for (String id : id_list)
			{
			final ResourceToken token = context.getProject().getResourceStorage().findResource(id);
			if (token == null)
				{
				final MuseEvent event = MessageEventType.create("Unable to find plugin: " + id);
				event.addTag(MuseEvent.ERROR);
				context.raiseEvent(event);
				}
			else if (token.getResource() instanceof PluginConfiguration)
                {
                boolean added = ((PluginConfiguration) token.getResource()).createPlugin().conditionallyAddToContext(context, false);
                context.raiseEvent(MessageEventType.create("plugin " + token.getId() + " added: " + added));
                }
            else
				{
				final MuseEvent event = MessageEventType.create(String.format("Resource %s is a %s. A PluginConfiguration is required", id, token.getResource().getType()));
				event.addTag(MuseEvent.ERROR);
				context.raiseEvent(event);
				}
			}

		return true;
		}

	@Override
	protected boolean applyToContextType(MuseExecutionContext context)
		{
		return true;
		}

	@Override
	public void initialize(MuseExecutionContext context)
		{
		}
	}
