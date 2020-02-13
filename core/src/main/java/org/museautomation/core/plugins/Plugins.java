package org.museautomation.core.plugins;

import org.museautomation.core.*;
import org.museautomation.core.resource.*;
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

    public static <T> T findType(Class<T> type, MuseExecutionContext context)
	    {
	    for (MusePlugin plugin : context.getPlugins())
	    	if (type.isInstance(plugin))
	    		return (T) plugin;

	    if (context.getParent() == null)
	        return null;
	    else
	    	return findType(type, context.getParent());  // recursively look in ancestor contexts
	    }


    private final static Logger LOG = LoggerFactory.getLogger(Plugins.class);
    }


