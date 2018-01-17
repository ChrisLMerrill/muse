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
	        ((TestPluginConfiguration) resource.getResource()).createPlugin().addToContext(context, true);
        }
    }


