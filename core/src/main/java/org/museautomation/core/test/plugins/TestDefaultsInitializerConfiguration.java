package org.museautomation.core.test.plugins;

import org.museautomation.core.*;
import org.museautomation.core.plugins.*;
import org.museautomation.core.resource.generic.*;
import org.museautomation.core.resource.types.*;
import org.museautomation.core.values.*;
import org.museautomation.core.values.descriptor.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("test-defaults-initializer")
@MuseSubsourceDescriptor(displayName = "Apply automatically?", description = "If this source resolves to true, this plugin configuration will be automatically applied to tests", type = SubsourceDescriptor.Type.Named, name = GenericConfigurablePlugin.AUTO_APPLY_PARAM)
@MuseSubsourceDescriptor(displayName = "Apply only if", description = "Apply only if this source this source resolves to true", type = SubsourceDescriptor.Type.Named, name = GenericConfigurablePlugin.APPLY_CONDITION_PARAM)
@MuseSubsourceDescriptor(displayName = "Overwrite", description = "If true, overwrite variables that have already been set by other initializers (default is false)", type = SubsourceDescriptor.Type.Named, name = TestDefaultsInitializerConfiguration.OVERWRITE_PARAM, optional = true)
@SuppressWarnings("unused")  // instantiated by reflection
public class TestDefaultsInitializerConfiguration extends GenericResourceConfiguration implements PluginConfiguration
	{
	@Override
	public ResourceType getType()
		{
		return new TestDefaultsInitializerType();
		}

	@Override
	public TestDefaultsInitializer createPlugin()
		{
		return new TestDefaultsInitializer(this);
		}

	public final static String TYPE_ID = TestDefaultsInitializerConfiguration.class.getAnnotation(MuseTypeId.class).value();
	final static String OVERWRITE_PARAM = "overwrite";

	@SuppressWarnings("unused") // used by reflection
	public static class TestDefaultsInitializerType extends ResourceSubtype
		{
		@Override
		public TestDefaultsInitializerConfiguration create()
			{
			final TestDefaultsInitializerConfiguration config = new TestDefaultsInitializerConfiguration();
			config.parameters().addSource(GenericConfigurablePlugin.AUTO_APPLY_PARAM, ValueSourceConfiguration.forValue(true));
			config.parameters().addSource(GenericConfigurablePlugin.APPLY_CONDITION_PARAM, ValueSourceConfiguration.forValue(true));
			config.parameters().addSource(OVERWRITE_PARAM, ValueSourceConfiguration.forValue(false));
			return config;
			}

		@Override
		public ResourceDescriptor getDescriptor()
			{
			return new DefaultResourceDescriptor(this, "Set default test variables into the context.");
			}

		@SuppressWarnings("WeakerAccess")  // instantiated by reflection
		public TestDefaultsInitializerType()
			{
			super(TYPE_ID, "Test Defaults Initializer", TestDefaultsInitializerConfiguration.class, new PluginConfigurationResourceType());
			}
		}
	}
