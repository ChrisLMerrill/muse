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
	 * Finds all the plugins in the project and applies them to the supplied context.
	 */
	public static void setup(MuseExecutionContext context)
		{
        final List<ResourceToken> resources = context.getProject().getResourceStorage().findResources(new ResourceQueryParameters(new PluginConfiguration.PluginConfigurationResourceType()));
        for (ResourceToken resource : resources)
	        {
	        final MusePlugin plugin = ((PluginConfiguration) resource.getResource()).createPlugin();
	        try
		        {
		        plugin.conditionallyAddToContext(context, true);
		        }
	        catch (MuseExecutionError e)
		        {
		        LOG.error("Unable to complete plugin setup for resource id " + resource.getId(), e);
		        }
	        }
        }

    private final static Logger LOG = LoggerFactory.getLogger(Plugins.class);
    }


