package org.museautomation.builtins.plugins.events;

import org.museautomation.core.*;
import org.museautomation.core.plugins.*;
import org.museautomation.core.resource.generic.*;
import org.museautomation.core.resource.types.*;
import org.museautomation.core.values.*;
import org.museautomation.core.values.descriptor.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("event-logger-plugin")
@MuseSubsourceDescriptor(displayName = "Apply automatically?", description = "If this source resolves to true, this plugin configuration will be automatically applied to tests", type = SubsourceDescriptor.Type.Named, name = GenericConfigurablePlugin.AUTO_APPLY_PARAM)
@MuseSubsourceDescriptor(displayName = "Apply only if", description = "Apply only if this source this source resolves to true", type = SubsourceDescriptor.Type.Named, name = GenericConfigurablePlugin.APPLY_CONDITION_PARAM)
@MuseSubsourceDescriptor(displayName = "Log as JSON", description = "Log events to JSON file", type = SubsourceDescriptor.Type.Named, name = EventLogWriterConfiguration.LOG_TO_JSON, optional = true, defaultValue = "true")
@MuseSubsourceDescriptor(displayName = "Log as text", description = "Log events in human-readable text format", type = SubsourceDescriptor.Type.Named, name = EventLogWriterConfiguration.LOG_TO_TEXT, optional = true, defaultValue = "true")
@MuseSubsourceDescriptor(displayName = "Log to stdout", description = "Log events to standard out", type = SubsourceDescriptor.Type.Named, name = EventLogWriterConfiguration.LOG_TO_STDOUT, optional = true, defaultValue = "false")
@SuppressWarnings("unused") // Instantiated by reflection
public class EventLogWriterConfiguration extends GenericResourceConfiguration implements PluginConfiguration
	{
	@Override
	public MusePlugin createPlugin()
		{
		return new EventLogWriterPlugin(this);
		}

	@Override
	public ResourceType getType()
		{
		return new EventLogWriterType();
		}

    boolean isLogToStdout(MuseExecutionContext context)
   		{
   		return isParameterTrue(context, LOG_TO_STDOUT, false);
   		}

    boolean isLogToJson(MuseExecutionContext context)
   		{
   		return isParameterTrue(context, LOG_TO_JSON, true);
   		}

    boolean isLogToText(MuseExecutionContext context)
   		{
   		return isParameterTrue(context, LOG_TO_TEXT, true);
   		}

	public final static String TYPE_ID = EventLogWriterConfiguration.class.getAnnotation(MuseTypeId.class).value();

	@SuppressWarnings("unused") // used by reflection
	public static class EventLogWriterType extends ResourceSubtype
		{
		@Override
		public EventLogWriterConfiguration create()
			{
			final EventLogWriterConfiguration config = new EventLogWriterConfiguration();
			config.parameters().addSource(GenericConfigurablePlugin.AUTO_APPLY_PARAM, ValueSourceConfiguration.forValue(true));
			config.parameters().addSource(GenericConfigurablePlugin.APPLY_CONDITION_PARAM, ValueSourceConfiguration.forValue(true));
			return config;
			}

		@SuppressWarnings("WeakerAccess")  // instantiated by reflection
		public EventLogWriterType()
			{
			super(TYPE_ID, "Event Log Writer", EventLogWriterConfiguration.class, new PluginConfigurationResourceType());
			}
		}

    final static String LOG_TO_JSON = "log-to-json";
    final static String LOG_TO_TEXT = "log-to-text";
    final static String LOG_TO_STDOUT = "log-to-stdout";
	}
