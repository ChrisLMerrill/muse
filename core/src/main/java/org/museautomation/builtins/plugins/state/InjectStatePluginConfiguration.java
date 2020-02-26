package org.museautomation.builtins.plugins.state;

import org.museautomation.core.*;
import org.museautomation.core.plugins.*;
import org.museautomation.core.resource.generic.*;
import org.museautomation.core.resource.types.*;
import org.museautomation.core.values.*;
import org.museautomation.core.values.descriptor.*;

import static org.museautomation.builtins.plugins.resultstorage.LocalStorageLocationPluginConfiguration.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("inject-state")
@MuseSubsourceDescriptor(displayName = "Apply automatically?", description = "If this source resolves to true, this plugin configuration will be automatically applied to tests", type = SubsourceDescriptor.Type.Named, name = GenericConfigurablePlugin.AUTO_APPLY_PARAM)
@MuseSubsourceDescriptor(displayName = "Apply only if", description = "Apply only if this source this source resolves to true", type = SubsourceDescriptor.Type.Named, name = GenericConfigurablePlugin.APPLY_CONDITION_PARAM)
@MuseSubsourceDescriptor(displayName = "Base location", description = "Provides the base folder where execution data will be stored.", type = SubsourceDescriptor.Type.Named, name = BASE_LOCATION_PARAM_NAME)
@SuppressWarnings("WeakerAccess")  // instantiated by reflection
public class InjectStatePluginConfiguration extends GenericResourceConfiguration implements PluginConfiguration
	{
	@Override
	public ResourceType getType()
		{
		return new LocalStorageLocationPluginConfigurationType();
		}

	@Override
	public InjectStatePlugin createPlugin()
		{
		return new InjectStatePlugin(this);
		}

	public final static String TYPE_ID = InjectStatePluginConfiguration.class.getAnnotation(MuseTypeId.class).value();

	public static class LocalStorageLocationPluginConfigurationType extends ResourceSubtype
		{
		@Override
		public InjectStatePluginConfiguration create()
			{
			final InjectStatePluginConfiguration config = new InjectStatePluginConfiguration();
			config.parameters().addSource(GenericConfigurablePlugin.AUTO_APPLY_PARAM, ValueSourceConfiguration.forValue(true));
			config.parameters().addSource(GenericConfigurablePlugin.APPLY_CONDITION_PARAM, ValueSourceConfiguration.forValue(true));
			return config;
			}

		@Override
		public ResourceDescriptor getDescriptor()
			{
			return new DefaultResourceDescriptor(this, "Injects the values from InterTaskState(s) into the execution context.");
			}

		public LocalStorageLocationPluginConfigurationType()
			{
			super(TYPE_ID, "Inject State", InjectStatePluginConfiguration.class, new PluginConfigurationResourceType());
			}
		}
	}


