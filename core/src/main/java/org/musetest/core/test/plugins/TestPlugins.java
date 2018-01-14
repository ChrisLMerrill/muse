package org.musetest.core.test.plugins;

import org.musetest.core.*;
import org.musetest.core.resource.*;
import org.musetest.core.test.plugin.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class TestPlugins
    {
	/**
	 * Finds all the plugins in the project and applies them to the supplied context.
	 */
	public static void setup(MuseExecutionContext context) throws MuseExecutionError
		{
        final List<ResourceToken> resources = context.getProject().getResourceStorage().findResources(new ResourceQueryParameters(new TestPluginConfiguration.TestPluginConfigurationResourceType()));
        for (ResourceToken resource : resources)
        	applyConditionally((TestPluginConfiguration) resource.getResource(), context);
		context.addTestPlugin(new TestDefaultsInitializer());
        }

    public static void applyConditionally(TestPluginConfiguration config, MuseExecutionContext context) throws MuseExecutionError
	    {
	    final TestPlugin plugin = config.createPlugin();
	    if (plugin.applyAutomatically(context))
		    if (plugin.applyToThisTest(context))
		        context.addTestPlugin(plugin);
	    }
    }


