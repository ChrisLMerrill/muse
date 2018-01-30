package org.musetest.report.junit;

import org.musetest.core.*;
import org.musetest.core.resource.generic.*;
import org.musetest.core.resource.types.*;
import org.musetest.core.suite.plugin.*;
import org.musetest.core.test.plugin.*;
import org.musetest.core.values.*;
import org.musetest.core.values.descriptor.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("junit-report-plugin")
@MuseSubsourceDescriptor(displayName = "Apply automatically?", description = "If this source resolves to true, this plugin configuration will be automatically applied to tests", type = SubsourceDescriptor.Type.Named, name = BaseTestPlugin.AUTO_APPLY_PARAM)
@MuseSubsourceDescriptor(displayName = "Apply only if", description = "Apply only if this source this source resolves to true", type = SubsourceDescriptor.Type.Named, name = BaseTestPlugin.APPLY_CONDITION_PARAM)
@SuppressWarnings("unused") // Instantiated by reflection
public class JUnitReportCollectorConfiguration extends GenericResourceConfiguration implements TestSuitePluginConfiguration
	{
	@Override
	public JUnitReportCollector createPlugin()
		{
		return new JUnitReportCollector(this);
		}

	@Override
	public ResourceType getType()
		{
		return new JUnitReportConfigurationType();
		}

	public final static String TYPE_ID = JUnitReportCollectorConfiguration.class.getAnnotation(MuseTypeId.class).value();

	@SuppressWarnings("unused") // used by reflection
	public static class JUnitReportConfigurationType extends ResourceSubtype
		{
		@Override
		public JUnitReportCollectorConfiguration create()
			{
			final JUnitReportCollectorConfiguration config = new JUnitReportCollectorConfiguration();
			config.parameters().addSource(BaseTestPlugin.AUTO_APPLY_PARAM, ValueSourceConfiguration.forValue(true));
			config.parameters().addSource(BaseTestPlugin.APPLY_CONDITION_PARAM, ValueSourceConfiguration.forValue(true));
			return config;
			}

		@SuppressWarnings("WeakerAccess")  // instantiated by reflection
		public JUnitReportConfigurationType()
			{
			super(TYPE_ID, "JUnit Report", JUnitReportCollectorConfiguration.class, new TestSuitePluginConfiguration.TestSuitePluginConfigurationResourceType());
			}
		}
	}

