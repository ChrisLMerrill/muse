package org.museautomation.builtins.plugins.junit;

import org.museautomation.core.*;
import org.museautomation.core.plugins.*;
import org.museautomation.core.resource.generic.*;
import org.museautomation.core.resource.types.*;
import org.museautomation.core.values.*;
import org.museautomation.core.values.descriptor.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("junit-report-plugin")
@MuseSubsourceDescriptor(displayName = "Apply automatically?", description = "If this source resolves to true, this plugin configuration will be automatically applied to tests", type = SubsourceDescriptor.Type.Named, name = GenericConfigurablePlugin.AUTO_APPLY_PARAM)
@MuseSubsourceDescriptor(displayName = "Apply only if", description = "Apply only if this source this source resolves to true", type = SubsourceDescriptor.Type.Named, name = GenericConfigurablePlugin.APPLY_CONDITION_PARAM)
@MuseSubsourceDescriptor(displayName = "Create Attachment Links", description = "Generates attachment links compatible with the 'Junit Attachments' plugin for Jenkins", type = SubsourceDescriptor.Type.Named, name = JUnitReportCollectorConfiguration.ATTACHMENT_PARAM, defaultValue = "false")
@SuppressWarnings("unused") // Instantiated by reflection
public class JUnitReportCollectorConfiguration extends GenericResourceConfiguration implements PluginConfiguration
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

    public boolean isOutputAttachmentLines(MuseExecutionContext context)
   		{
   		return isParameterTrue(context, ATTACHMENT_PARAM, true);
   		}

	public final static String TYPE_ID = JUnitReportCollectorConfiguration.class.getAnnotation(MuseTypeId.class).value();

	public final static String ATTACHMENT_PARAM = "attachment-lines";

	@SuppressWarnings("unused") // used by reflection
	public static class JUnitReportConfigurationType extends ResourceSubtype
		{
		@Override
		public JUnitReportCollectorConfiguration create()
			{
			final JUnitReportCollectorConfiguration config = new JUnitReportCollectorConfiguration();
			config.parameters().addSource(GenericConfigurablePlugin.AUTO_APPLY_PARAM, ValueSourceConfiguration.forValue(true));
			config.parameters().addSource(GenericConfigurablePlugin.APPLY_CONDITION_PARAM, ValueSourceConfiguration.forValue(true));
			return config;
			}

		@SuppressWarnings("WeakerAccess")  // instantiated by reflection
		public JUnitReportConfigurationType()
			{
			super(TYPE_ID, "JUnit Report", JUnitReportCollectorConfiguration.class, new PluginConfiguration.PluginConfigurationResourceType());
			}
		}
	}


