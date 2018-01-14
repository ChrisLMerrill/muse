package org.musetest.core.test.plugin;

import org.musetest.core.resource.types.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public interface TestPluginConfiguration
	{
	TestPlugin createPlugin();

	class TestPluginConfigurationResourceType extends ResourceType
		{
		public TestPluginConfigurationResourceType()
			{
			super("test-plugin", "Test Plugin", TestPluginConfiguration.class);
			}
		}
	}


