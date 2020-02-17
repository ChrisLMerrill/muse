package org.museautomation.builtins.plugins.delay;

import org.museautomation.core.*;
import org.museautomation.core.plugins.*;
import org.museautomation.core.resource.generic.*;
import org.museautomation.core.resource.types.*;
import org.museautomation.core.values.*;
import org.museautomation.core.values.descriptor.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("step-wait-plugin")
@MuseSubsourceDescriptor(displayName = "Apply automatically?", description = "If this source resolves to true, this plugin configuration will be automatically applied to tests", type = SubsourceDescriptor.Type.Named, name = GenericConfigurablePlugin.AUTO_APPLY_PARAM)
@MuseSubsourceDescriptor(displayName = "Apply only if", description = "Apply only if this source this source resolves to true", type = SubsourceDescriptor.Type.Named, name = GenericConfigurablePlugin.APPLY_CONDITION_PARAM)
@MuseSubsourceDescriptor(displayName = "Delay (ms)", description = "The time to wait after each step", type = SubsourceDescriptor.Type.Named, name = StepWaitPluginConfiguration.DELAY_TIME, optional = true)
public class StepWaitPluginConfiguration extends GenericResourceConfiguration implements PluginConfiguration
    {
    @Override
    public ResourceType getType()
        {
        return new StepDelayPluginConfigurationType();
        }

    @Override
    public MusePlugin createPlugin()
        {
        return new StepWaitPlugin(this);
        }

    public final static String TYPE_ID = StepWaitPluginConfiguration.class.getAnnotation(MuseTypeId.class).value();
    public final static String DELAY_TIME = "delay_ms";
    public final static Long DEFAULT_DELAY_TIME = 1000L;

    public static class StepDelayPluginConfigurationType extends ResourceSubtype
   		{
   		@Override
   		public StepWaitPluginConfiguration create()
   			{
   			final StepWaitPluginConfiguration config = new StepWaitPluginConfiguration();
   			config.parameters().addSource(GenericConfigurablePlugin.AUTO_APPLY_PARAM, ValueSourceConfiguration.forValue(true));
   			config.parameters().addSource(GenericConfigurablePlugin.APPLY_CONDITION_PARAM, ValueSourceConfiguration.forValue(true));
   			config.parameters().addSource(DELAY_TIME, ValueSourceConfiguration.forValue(DEFAULT_DELAY_TIME));
   			return config;
   			}

   		@SuppressWarnings("WeakerAccess")  // instantiated by reflection
   		public StepDelayPluginConfigurationType()
   			{
   			super(TYPE_ID, "Wait After Step plugin", StepWaitPluginConfiguration.class, new PluginConfigurationResourceType());
   			}
   		}

    }