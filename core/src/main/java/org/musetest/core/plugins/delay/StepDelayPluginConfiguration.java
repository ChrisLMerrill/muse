package org.musetest.core.plugins.delay;

import org.musetest.core.*;
import org.musetest.core.plugins.*;
import org.musetest.core.resource.generic.*;
import org.musetest.core.resource.types.*;
import org.musetest.core.values.*;
import org.musetest.core.values.descriptor.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("step-delay-plugin")
@MuseSubsourceDescriptor(displayName = "Apply automatically?", description = "If this source resolves to true, this plugin configuration will be automatically applied to tests", type = SubsourceDescriptor.Type.Named, name = GenericConfigurablePlugin.AUTO_APPLY_PARAM)
@MuseSubsourceDescriptor(displayName = "Apply only if", description = "Apply only if this source this source resolves to true", type = SubsourceDescriptor.Type.Named, name = GenericConfigurablePlugin.APPLY_CONDITION_PARAM)
public class StepDelayPluginConfiguration extends GenericResourceConfiguration implements PluginConfiguration
    {
    @Override
    public ResourceType getType()
        {
        return new StepDelayPluginConfigurationType();
        }

    @Override
    public MusePlugin createPlugin()
        {
        return new StepDelayPlugin(this);
        }

    public final static String TYPE_ID = StepDelayPluginConfiguration.class.getAnnotation(MuseTypeId.class).value();
    public final static String DELAY_TIME = "delay_ms";
    public final static Long DEFAULT_DELAY_TIME = 1000L;

    public static class StepDelayPluginConfigurationType extends ResourceSubtype
   		{
   		@Override
   		public StepDelayPluginConfiguration create()
   			{
   			final StepDelayPluginConfiguration config = new StepDelayPluginConfiguration();
   			config.parameters().addSource(GenericConfigurablePlugin.AUTO_APPLY_PARAM, ValueSourceConfiguration.forValue(true));
   			config.parameters().addSource(GenericConfigurablePlugin.APPLY_CONDITION_PARAM, ValueSourceConfiguration.forValue(true));
   			config.parameters().addSource(DELAY_TIME, ValueSourceConfiguration.forValue(DEFAULT_DELAY_TIME));
   			return config;
   			}

   		@SuppressWarnings("WeakerAccess")  // instantiated by reflection
   		public StepDelayPluginConfigurationType()
   			{
   			super(TYPE_ID, "Step Delay plugin", StepDelayPluginConfiguration.class, new PluginConfigurationResourceType());
   			}
   		}

    }