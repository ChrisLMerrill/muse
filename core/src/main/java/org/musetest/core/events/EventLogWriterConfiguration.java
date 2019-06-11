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
	}
