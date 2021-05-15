package org.museautomation.builtins.network;

import org.museautomation.core.*;
import org.museautomation.core.plugins.*;
import org.museautomation.core.resource.generic.*;
import org.museautomation.core.resource.types.*;
import org.museautomation.core.values.*;
import org.museautomation.core.values.descriptor.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("network-proxy")
@MuseSubsourceDescriptor(displayName = "Apply automatically?", description = "If this source resolves to true, this plugin configuration will be automatically applied to tests", type = SubsourceDescriptor.Type.Named, name = GenericConfigurablePlugin.AUTO_APPLY_PARAM)
@MuseSubsourceDescriptor(displayName = "Apply only if", description = "Apply only if this source this source resolves to true", type = SubsourceDescriptor.Type.Named, name = GenericConfigurablePlugin.APPLY_CONDITION_PARAM)
@MuseSubsourceDescriptor(displayName = "Proxy Configuration", description = "Specifies the proxy configuration to be used. This should resolve to either a proxy configuration resource in the project or the id of one.", type = SubsourceDescriptor.Type.Named, name = NetworkProxyProviderPluginConfiguration.PROXY_CONFIG_PARAM_NAME)
@SuppressWarnings("WeakerAccess")  // instantiated by reflection
public class NetworkProxyProviderPluginConfiguration extends GenericResourceConfiguration implements PluginConfiguration
	{
	@Override
	public ResourceType getType()
		{
		return new NetworkProxyProviderPluginConfigurationType();
		}

	@Override
	public NetworkProxyProviderPlugin createPlugin()
		{
		return new NetworkProxyProviderPlugin(this);
		}

	public final static String TYPE_ID = NetworkProxyProviderPluginConfiguration.class.getAnnotation(MuseTypeId.class).value();
	public final static String PROXY_CONFIG_PARAM_NAME = "proxy-config";

	public static class NetworkProxyProviderPluginConfigurationType extends ResourceSubtype
		{
		@Override
		public NetworkProxyProviderPluginConfiguration create()
			{
			final NetworkProxyProviderPluginConfiguration config = new NetworkProxyProviderPluginConfiguration();
			config.parameters().addSource(GenericConfigurablePlugin.AUTO_APPLY_PARAM, ValueSourceConfiguration.forValue(true));
			config.parameters().addSource(GenericConfigurablePlugin.APPLY_CONDITION_PARAM, ValueSourceConfiguration.forValue(true));
			config.parameters().addSource(PROXY_CONFIG_PARAM_NAME, ValueSourceConfiguration.forValue(""));
			return config;
			}

		@Override
		public ResourceDescriptor getDescriptor()
			{
			return new DefaultResourceDescriptor(this, "Provides a network proxy configuration to be used by other automation components.");
			}

		public NetworkProxyProviderPluginConfigurationType()
			{
			super(TYPE_ID, "Network Proxy", NetworkProxyProviderPluginConfiguration.class, new PluginConfigurationResourceType());
			}
		}
	}


