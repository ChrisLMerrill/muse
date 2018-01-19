package org.musetest.core.resultstorage;

import org.musetest.core.*;
import org.musetest.core.resource.generic.*;
import org.musetest.core.resource.types.*;
import org.musetest.core.test.plugin.*;
import org.musetest.core.values.*;
import org.musetest.core.values.descriptor.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("save-results-to-disk")
@MuseSubsourceDescriptor(displayName = "Apply automatically?", description = "If this source resolves to true, this plugin configuration will be automatically applied to tests", type = SubsourceDescriptor.Type.Named, name = BaseTestPlugin.AUTO_APPLY_PARAM)
@MuseSubsourceDescriptor(displayName = "Apply only if", description = "Apply only if this source this source resolves to true", type = SubsourceDescriptor.Type.Named, name = BaseTestPlugin.APPLY_CONDITION_PARAM)
@SuppressWarnings("WeakerAccess")  // instantiated by reflection
public class SaveTestResultsToDiskConfiguration extends GenericResourceConfiguration implements TestPluginConfiguration
	{
	@Override
	public ResourceType getType()
		{
		return new SaveTestResultsToDiskConfigurationType();
		}

	@Override
	public SaveTestResultsToDisk createPlugin()
		{
		return new SaveTestResultsToDisk(this);
		}

	public final static String TYPE_ID = SaveTestResultsToDiskConfiguration.class.getAnnotation(MuseTypeId.class).value();

	public static class SaveTestResultsToDiskConfigurationType extends ResourceSubtype
		{
		@Override
		public SaveTestResultsToDiskConfiguration create()
			{
			final SaveTestResultsToDiskConfiguration config = new SaveTestResultsToDiskConfiguration();
			config.parameters().addSource(BaseTestPlugin.AUTO_APPLY_PARAM, ValueSourceConfiguration.forValue(true));
			config.parameters().addSource(BaseTestPlugin.APPLY_CONDITION_PARAM, ValueSourceConfiguration.forValue(true));
			return config;
			}

		public SaveTestResultsToDiskConfigurationType()
			{
			super(TYPE_ID, "Results Saver", SaveTestResultsToDiskConfiguration.class, new TestPluginConfigurationResourceType());
			}
		}
	}


