package org.musetest.core.test.plugins;

import org.musetest.core.*;
import org.musetest.core.resource.generic.*;
import org.musetest.core.resource.types.*;
import org.musetest.core.test.plugin.*;
import org.musetest.core.values.*;
import org.musetest.core.values.descriptor.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("test-defaults-initializer")
@MuseSubsourceDescriptor(displayName = "Apply automatically?", description = "If this source resolves to true, this plugin configuration will be automatically applied to tests", type = SubsourceDescriptor.Type.Named, name = BaseTestPlugin.AUTO_APPLY_PARAM)
@MuseSubsourceDescriptor(displayName = "Apply only if", description = "Apply only if this source this source resolves to true", type = SubsourceDescriptor.Type.Named, name = BaseTestPlugin.APPLY_CONDITION_PARAM)
@MuseSubsourceDescriptor(displayName = "Overwrite", description = "If true, overwrite variables that have already been set by other initializers (default is true)", type = SubsourceDescriptor.Type.Named, name = TestDefaultsInitializerConfiguration.OVERWRITE_PARAM, optional = true)
@SuppressWarnings("unused")  // instantiated by reflection
public class TestDefaultsInitializerConfiguration extends GenericResourceConfiguration implements TestPluginConfiguration
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
	public final static String OVERWRITE_PARAM = "overwrite";

	@SuppressWarnings("unused") // used by reflection
	public static class TestDefaultsInitializerType extends ResourceSubtype
		{
		@Override
		public TestDefaultsInitializerConfiguration create()
			{
			final TestDefaultsInitializerConfiguration config = new TestDefaultsInitializerConfiguration();
			config.parameters().addSource(BaseTestPlugin.AUTO_APPLY_PARAM, ValueSourceConfiguration.forValue(true));
			config.parameters().addSource(BaseTestPlugin.APPLY_CONDITION_PARAM, ValueSourceConfiguration.forValue(true));
			config.parameters().addSource(OVERWRITE_PARAM, ValueSourceConfiguration.forValue(true));
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
			super(TYPE_ID, "Test Defaults Initializer", TestDefaultsInitializerConfiguration.class, new TestPluginConfigurationResourceType());
			}
		}
	}
