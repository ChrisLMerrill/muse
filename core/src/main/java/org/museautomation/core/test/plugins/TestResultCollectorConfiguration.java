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
@MuseTypeId("test-result-collector")
@MuseSubsourceDescriptor(displayName = "Apply automatically?", description = "If this source resolves to true, this plugin configuration will be automatically applied to tests", type = SubsourceDescriptor.Type.Named, name = GenericConfigurablePlugin.AUTO_APPLY_PARAM)
@MuseSubsourceDescriptor(displayName = "Apply only if", description = "Apply only if this source this source resolves to true", type = SubsourceDescriptor.Type.Named, name = GenericConfigurablePlugin.APPLY_CONDITION_PARAM)
@MuseSubsourceDescriptor(displayName = "Fail on failure", description = "Test fails if any step reports failure (default is true)", type = SubsourceDescriptor.Type.Named, name = TestResultCollectorConfiguration.FAIL_ON_FAILURE, optional = true)
@MuseSubsourceDescriptor(displayName = "Fail on error", description = "Test fails if any step reports an error (default is true)", type = SubsourceDescriptor.Type.Named, name = TestResultCollectorConfiguration.FAIL_ON_ERROR, optional = true)
@MuseSubsourceDescriptor(displayName = "Fail on interrupt", description = "Test fails if the test is interrupted (default is true)", type = SubsourceDescriptor.Type.Named, name = TestResultCollectorConfiguration.FAIL_ON_INTERRUPT, optional = true)
@SuppressWarnings("WeakerAccess")  // instantiated by reflection
public class TestResultCollectorConfiguration extends GenericResourceConfiguration implements PluginConfiguration
	{
	@Override
	public ResourceType getType()
		{
		return new TestResultCollectorConfigurationType();
		}

	@Override
	public MusePlugin createPlugin()
		{
		return new TestResultCollector(this);
		}

	public final static String TYPE_ID = TestResultCollectorConfiguration.class.getAnnotation(MuseTypeId.class).value();
	public final static String FAIL_ON_INTERRUPT = "fail-on-interrupt";
	public final static String FAIL_ON_FAILURE = "fail-on-failure";
	public final static String FAIL_ON_ERROR = "fail-on-error";

	public static class TestResultCollectorConfigurationType extends ResourceSubtype
		{
		@Override
		public TestResultCollectorConfiguration create()
			{
			final TestResultCollectorConfiguration config = new TestResultCollectorConfiguration();
			config.parameters().addSource(GenericConfigurablePlugin.AUTO_APPLY_PARAM, ValueSourceConfiguration.forValue(true));
			config.parameters().addSource(GenericConfigurablePlugin.APPLY_CONDITION_PARAM, ValueSourceConfiguration.forValue(true));
			config.parameters().addSource(FAIL_ON_ERROR, ValueSourceConfiguration.forValue(true));
			config.parameters().addSource(FAIL_ON_FAILURE, ValueSourceConfiguration.forValue(true));
			config.parameters().addSource(FAIL_ON_INTERRUPT, ValueSourceConfiguration.forValue(true));
			return config;
			}

		@SuppressWarnings("WeakerAccess")  // instantiated by reflection
		public TestResultCollectorConfigurationType()
			{
			super(TYPE_ID, "Test Result Calculator", TestResultCollector.class, new PluginConfigurationResourceType());
			}
		}
	}
