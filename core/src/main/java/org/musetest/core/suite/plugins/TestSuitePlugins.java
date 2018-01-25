package org.musetest.core.suite.plugins;

import org.musetest.core.*;
import org.musetest.core.resource.*;
import org.musetest.core.suite.plugin.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class TestSuitePlugins
    {
	/**
	 * Finds all the plugins in the project and applies them to the supplied context.
	 */
	public static List<TestSuitePlugin> locateAutomatic(MuseExecutionContext context, MuseTestSuite suite) throws MuseExecutionError
		{
        final List<ResourceToken> resources = context.getProject().getResourceStorage().findResources(new ResourceQueryParameters(new TestSuitePluginConfiguration.TestSuitePluginConfigurationResourceType()));
		List<TestSuitePlugin> plugins = new ArrayList<>();
        for (ResourceToken resource : resources)
	        {
	        final TestSuitePlugin plugin = ((TestSuitePluginConfiguration) resource.getResource()).createPlugin();
	        if (plugin.shouldAddToSuite(context, suite, true))
	        	plugins.add(plugin);
	        }
		return plugins;
        }
    }


