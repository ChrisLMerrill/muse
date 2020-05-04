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
        final List<ResourceToken<MuseResource>> resources = context.getProject().getResourceStorage().findResources(new ResourceQueryParameters(new PluginConfiguration.PluginConfigurationResourceType()));
        for (ResourceToken<MuseResource> resource : resources)
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

    public static <T> List<T> findAll(Class<T> type, MuseExecutionContext context)
	    {
	    List<T> found = new ArrayList<>();
	    for (MusePlugin plugin : context.getPlugins())
	    	if (type.isInstance(plugin))
	    		found.add((T) plugin);

	    if (context.getParent() != null)
            {
            List<T> from_parent = findAll(type, context.getParent());
            for (T plugin : from_parent)
                if (!found.contains(plugin)) // remove duplicates
                    found.add(plugin);
            }
	    return found;
        }

    private final static Logger LOG = LoggerFactory.getLogger(Plugins.class);
    }