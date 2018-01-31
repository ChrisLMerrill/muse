package org.musetest.core.suite.plugins;

import org.musetest.core.*;
import org.musetest.core.plugins.*;
import org.musetest.core.resource.generic.*;
import org.musetest.core.resource.types.*;
import org.musetest.core.values.*;
import org.musetest.core.values.descriptor.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("test-suite-result-counter")
@MuseSubsourceDescriptor(displayName = "Apply automatically?", description = "If this source resolves to true, this plugin configuration will be automatically applied to tests", type = SubsourceDescriptor.Type.Named, name = GenericConfigurablePlugin.AUTO_APPLY_PARAM)
@MuseSubsourceDescriptor(displayName = "Apply only if", description = "Apply only if this source this source resolves to true", type = SubsourceDescriptor.Type.Named, name = GenericConfigurablePlugin.APPLY_CONDITION_PARAM)
@SuppressWarnings("unused") // used by reflection
public class TestSuiteResultCounterConfiguration extends GenericResourceConfiguration implements PluginConfiguration
	{
	@Override
	public TestSuiteResultCounter createPlugin()
		{
		return new TestSuiteResultCounter(this);
		}

	@Override
	public ResourceType getType()
		{
		return new TestSuiteResultCounterType();
		}

	public final static String TYPE_ID = TestSuiteResultCounterConfiguration.class.getAnnotation(MuseTypeId.class).value();

	@SuppressWarnings("unused") // used by reflection
	public static class TestSuiteResultCounterType extends ResourceSubtype
		{
		@Override
		public TestSuiteResultCounterConfiguration create()
			{
			final TestSuiteResultCounterConfiguration config = new TestSuiteResultCounterConfiguration();
			config.parameters().addSource(GenericConfigurablePlugin.AUTO_APPLY_PARAM, ValueSourceConfiguration.forValue(true));
			config.parameters().addSource(GenericConfigurablePlugin.APPLY_CONDITION_PARAM, ValueSourceConfiguration.forValue(true));
			return config;
			}

		@Override
		public ResourceDescriptor getDescriptor()
			{
			return new DefaultResourceDescriptor(this, "Summarize the results of all tests in the suite");
			}

		@SuppressWarnings("WeakerAccess")  // instantiated by reflection
		public TestSuiteResultCounterType()
			{
			super(TYPE_ID, "Test Suite Result Counter", TestSuiteResultCounter.class, new PluginConfiguration.PluginConfigurationResourceType());
			}
		}

	}


