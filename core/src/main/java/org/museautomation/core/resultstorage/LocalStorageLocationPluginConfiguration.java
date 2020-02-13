package org.museautomation.core.resultstorage;

import org.museautomation.core.*;
import org.museautomation.core.plugins.*;
import org.museautomation.core.resource.generic.*;
import org.museautomation.core.resource.types.*;
import org.museautomation.core.values.*;
import org.museautomation.core.values.descriptor.*;

import static org.museautomation.core.resultstorage.LocalStorageLocationPluginConfiguration.BASE_LOCATION_PARAM_NAME;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("local-storage-location")
@MuseSubsourceDescriptor(displayName = "Apply automatically?", description = "If this source resolves to true, this plugin configuration will be automatically applied to tests", type = SubsourceDescriptor.Type.Named, name = GenericConfigurablePlugin.AUTO_APPLY_PARAM)
@MuseSubsourceDescriptor(displayName = "Apply only if", description = "Apply only if this source this source resolves to true", type = SubsourceDescriptor.Type.Named, name = GenericConfigurablePlugin.APPLY_CONDITION_PARAM)
@MuseSubsourceDescriptor(displayName = "Base location", description = "Provides the base folder where execution data will be stored.", type = SubsourceDescriptor.Type.Named, name = BASE_LOCATION_PARAM_NAME)
@SuppressWarnings("WeakerAccess")  // instantiated by reflection
public class LocalStorageLocationPluginConfiguration extends GenericResourceConfiguration implements PluginConfiguration
	{
	@Override
	public ResourceType getType()
		{
		return new SaveTestResultsToDiskConfigurationType();
		}

	@Override
	public LocalStorageLocationPlugin createPlugin()
		{
		return new LocalStorageLocationPlugin(this);
		}

	public final static String TYPE_ID = LocalStorageLocationPluginConfiguration.class.getAnnotation(MuseTypeId.class).value();
	public final static String BASE_LOCATION_PARAM_NAME = "base-param";

	public static class SaveTestResultsToDiskConfigurationType extends ResourceSubtype
		{
		@Override
		public LocalStorageLocationPluginConfiguration create()
			{
			final LocalStorageLocationPluginConfiguration config = new LocalStorageLocationPluginConfiguration();
			config.parameters().addSource(GenericConfigurablePlugin.AUTO_APPLY_PARAM, ValueSourceConfiguration.forValue(true));
			config.parameters().addSource(GenericConfigurablePlugin.APPLY_CONDITION_PARAM, ValueSourceConfiguration.forValue(true));
			config.parameters().addSource(BASE_LOCATION_PARAM_NAME, ValueSourceConfiguration.forValue("C:\\Temp\\muse-output\\"));
			return config;
			}

		@Override
		public ResourceDescriptor getDescriptor()
			{
			return new DefaultResourceDescriptor(this, "Determines where execution data should be stored locally. Other plugins may use this to decide where to store their data");
			}

		public SaveTestResultsToDiskConfigurationType()
			{
			super(TYPE_ID, "Local Storage Location", LocalStorageLocationPluginConfiguration.class, new PluginConfigurationResourceType());
			}
		}
	}


