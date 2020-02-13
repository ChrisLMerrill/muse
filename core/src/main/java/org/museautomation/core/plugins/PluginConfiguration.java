package org.museautomation.core.plugins;

import org.museautomation.core.resource.types.*;

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


