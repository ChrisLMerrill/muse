package org.musetest.core.plugins;

import org.musetest.core.resource.types.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public interface PluginConfiguration
	{
	MusePlugin createPlugin();

	class PluginConfigurationResourceType extends ResourceType
		{
		public PluginConfigurationResourceType()
			{
			super("plugin", "Plugin", PluginConfiguration.class);
			}
		}
	}


