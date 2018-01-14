package org.musetest.core.events;

import org.musetest.core.*;
import org.musetest.core.resource.generic.*;
import org.musetest.core.resource.types.*;
import org.musetest.core.test.plugin.*;
import org.musetest.core.values.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("event-logger-plugin")
@SuppressWarnings("unused") // Instantiated by reflection
public class EventLoggerConfiguration extends GenericResourceConfiguration implements TestPluginConfiguration
	{
	@Override
	public TestPlugin createPlugin()
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
			config.parameters().addSource(BaseTestPlugin.AUTO_APPLY_PARAM, ValueSourceConfiguration.forValue(true));
			config.parameters().addSource(BaseTestPlugin.APPLY_CONDITION_PARAM, ValueSourceConfiguration.forValue(true));
			return config;
			}

		@SuppressWarnings("WeakerAccess")  // instantiated by reflection
		public EventLoggerType()
			{
			super(TYPE_ID, "Event Logger", EventLoggerConfiguration.class, new TestPluginConfigurationResourceType());
			}
		}
	}
