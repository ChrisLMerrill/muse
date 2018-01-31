package org.musetest.core.test.plugins;

import org.musetest.core.*;
import org.musetest.core.plugins.*;
import org.musetest.core.resource.generic.*;
import org.musetest.core.resource.types.*;
import org.musetest.core.values.*;
import org.musetest.core.values.descriptor.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("compound-plugin")
@MuseSubsourceDescriptor(displayName = "Apply automatically?", description = "If this source resolves to true, this plugin configuration will be automatically applied to tests", type = SubsourceDescriptor.Type.Named, name = GenericConfigurablePlugin.AUTO_APPLY_PARAM)
@MuseSubsourceDescriptor(displayName = "Apply only if", description = "Apply only if this source this source resolves to true", type = SubsourceDescriptor.Type.Named, name = GenericConfigurablePlugin.APPLY_CONDITION_PARAM)
@MuseSubsourceDescriptor(displayName = "Plugin list", description = "Comma-separated list of plugin ids to apply", type = SubsourceDescriptor.Type.Named, name = CompoundPluginConfiguration.LISTS_PARAM)
public class CompoundPluginConfiguration extends GenericResourceConfiguration implements PluginConfiguration
	{
	@Override
	public ResourceType getType()
		{
		return new CompoundPluginType();
		}

	@Override
	public CompoundPlugin createPlugin()
		{
		return new CompoundPlugin(this);
		}

	public final static String TYPE_ID = CompoundPluginConfiguration.class.getAnnotation(MuseTypeId.class).value();
	public final static String LISTS_PARAM = "lists";

	@SuppressWarnings("unused") // used by reflection
	public static class CompoundPluginType extends ResourceSubtype
		{
		@Override
		public MuseResource create()
			{
			final CompoundPluginConfiguration config = new CompoundPluginConfiguration();
			config.parameters().addSource(GenericConfigurablePlugin.AUTO_APPLY_PARAM, ValueSourceConfiguration.forValue(true));
			config.parameters().addSource(GenericConfigurablePlugin.APPLY_CONDITION_PARAM, ValueSourceConfiguration.forValue(true));
			config.parameters().addSource(LISTS_PARAM, ValueSourceConfiguration.forValue("list1"));
			return config;
			}

		@Override
		public ResourceDescriptor getDescriptor()
			{
			return new DefaultResourceDescriptor(this, "Applies a list of plugins");
			}

		@SuppressWarnings("WeakerAccess")  // instantiated by reflection
		public CompoundPluginType()
			{
			super(TYPE_ID, "List of Plugins", CompoundPluginConfiguration.class, new PluginConfigurationResourceType());
			}
		}
	}


