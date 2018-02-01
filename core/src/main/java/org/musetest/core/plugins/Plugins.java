package org.musetest.core.plugins;

import org.musetest.core.*;
import org.musetest.core.resource.*;
import org.slf4j.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class Plugins
    {
	/**
	 * Finds all the plugins in the project and adds them to the supplied context. Returns a list of the plugins that were added.
	 */
	public static List<MusePlugin> setup(MuseExecutionContext context)
		{
		List<MusePlugin> applied_plugins = new ArrayList<>();
        final List<ResourceToken> resources = context.getProject().getResourceStorage().findResources(new ResourceQueryParameters(new PluginConfiguration.PluginConfigurationResourceType()));
        for (ResourceToken resource : resources)
	        {
	        final MusePlugin plugin = ((PluginConfiguration) resource.getResource()).createPlugin();
	        try
		        {
		        if (plugin.conditionallyAddToContext(context, true))
		        	applied_plugins.add(plugin);
		        }
	        catch (MuseExecutionError e)
		        {
		        LOG.error("Unable to complete plugin setup for resource id " + resource.getId(), e);
		        }
	        }
		return applied_plugins;
        }

    private final static Logger LOG = LoggerFactory.getLogger(Plugins.class);
    }


