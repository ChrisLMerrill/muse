package org.musetest.core.suite.plugin;

import org.musetest.core.resource.types.*;
import org.musetest.core.test.plugin.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public interface TestSuitePluginConfiguration
	{
	TestSuitePlugin createPlugin();

	class TestSuitePluginConfigurationResourceType extends ResourceType
		{
		public TestSuitePluginConfigurationResourceType()
			{
			super("test-suite-plugin", "Test Suite Plugin", TestSuitePluginConfiguration.class);
			}
		}
	}


