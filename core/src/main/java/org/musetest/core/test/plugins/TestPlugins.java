package org.musetest.core.test.plugins;

import org.musetest.core.*;
import org.musetest.core.resource.*;

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
        final List<ResourceToken> resources = context.getProject().getResourceStorage().findResources(new ResourceQueryParameters(new TestPluginsConfiguration.TestPluginsConfigurationResourceType()));
        for (ResourceToken resource : resources)
        	applyConditionally((TestPluginsConfiguration) resource.getResource(), context);
        }

    public static void applyConditionally(TestPluginsConfiguration configs, MuseExecutionContext context) throws MuseExecutionError
	    {
	    if (configs.shouldApplyToTest(context))
		    for (TestPluginConfiguration config : configs.getPlugins())
			    if (config.shouldApply(context))
			    	context.addTestPlugin(config.createPlugin(context.getProject()));
	    }
    }


