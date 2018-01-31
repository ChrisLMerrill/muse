package org.musetest.core.events;

import org.musetest.core.*;
import org.musetest.core.plugins.*;
import org.musetest.core.resource.generic.*;
import org.musetest.core.resource.types.*;
import org.musetest.core.values.*;
import org.musetest.core.values.descriptor.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("event-logger-plugin")
@MuseSubsourceDescriptor(displayName = "Apply automatically?", description = "If this source resolves to true, this plugin configuration will be automatically applied to tests", type = SubsourceDescriptor.Type.Named, name = GenericConfigurablePlugin.AUTO_APPLY_PARAM)
@MuseSubsourceDescriptor(displayName = "Apply only if", description = "Apply only if this source this source resolves to true", type = SubsourceDescriptor.Type.Named, name = GenericConfigurablePlugin.APPLY_CONDITION_PARAM)
@SuppressWarnings("unused") // Instantiated by reflection
public class EventLoggerConfiguration extends GenericResourceConfiguration implements PluginConfiguration
	{
	@Override
	public MusePlugin createPlugin()
		{
		return new EventLogger(this);
		}

	@Override
	public ResourceType getType()
		{
		return new EventLoggerType();
		}

	public final static String TYPE_ID = EventLoggerConfiguration.class.getAnnotation(MuseTypeId.class).value();

	@SuppressWarnings("unused") // used by reflection
	public static class EventLoggerType extends ResourceSubtype
		{
		@Override
		public EventLoggerConfiguration create()
			{
			final EventLoggerConfiguration config = new EventLoggerConfiguration();
			config.parameters().addSource(GenericConfigurablePlugin.AUTO_APPLY_PARAM, ValueSourceConfiguration.forValue(true));
			config.parameters().addSource(GenericConfigurablePlugin.APPLY_CONDITION_PARAM, ValueSourceConfiguration.forValue(true));
			return config;
			}

		@SuppressWarnings("WeakerAccess")  // instantiated by reflection
		public EventLoggerType()
			{
			super(TYPE_ID, "Event Logger", EventLoggerConfiguration.class, new PluginConfigurationResourceType());
			}
		}
	}
