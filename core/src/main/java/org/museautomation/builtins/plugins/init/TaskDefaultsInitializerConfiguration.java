package org.museautomation.builtins.plugins.init;

import org.museautomation.core.*;
import org.museautomation.core.plugins.*;
import org.museautomation.core.resource.generic.*;
import org.museautomation.core.resource.types.*;
import org.museautomation.core.values.*;
import org.museautomation.core.values.descriptor.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("task-defaults-initializer")
@MuseSubsourceDescriptor(displayName = "Apply automatically?", description = "If this source resolves to true, this plugin configuration will be automatically applied to tasks", type = SubsourceDescriptor.Type.Named, name = GenericConfigurablePlugin.AUTO_APPLY_PARAM)
@MuseSubsourceDescriptor(displayName = "Apply only if", description = "Apply only if this source this source resolves to true", type = SubsourceDescriptor.Type.Named, name = GenericConfigurablePlugin.APPLY_CONDITION_PARAM)
@MuseSubsourceDescriptor(displayName = "Overwrite", description = "If true, overwrite variables that have already been set by other initializers (default is false)", type = SubsourceDescriptor.Type.Named, name = TaskDefaultsInitializerConfiguration.OVERWRITE_PARAM, optional = true)
@SuppressWarnings("unused")  // instantiated by reflection
public class TaskDefaultsInitializerConfiguration extends GenericResourceConfiguration implements PluginConfiguration
	{
	@Override
	public ResourceType getType()
		{
		return new TaskDefaultsInitializerType();
		}

	@Override
	public TaskDefaultsInitializer createPlugin()
		{
		return new TaskDefaultsInitializer(this);
		}

	public final static String TYPE_ID = TaskDefaultsInitializerConfiguration.class.getAnnotation(MuseTypeId.class).value();
	final static String OVERWRITE_PARAM = "overwrite";

	@SuppressWarnings("unused") // used by reflection
	public static class TaskDefaultsInitializerType extends ResourceSubtype
		{
		@Override
		public TaskDefaultsInitializerConfiguration create()
			{
			final TaskDefaultsInitializerConfiguration config = new TaskDefaultsInitializerConfiguration();
			config.parameters().addSource(GenericConfigurablePlugin.AUTO_APPLY_PARAM, ValueSourceConfiguration.forValue(true));
			config.parameters().addSource(GenericConfigurablePlugin.APPLY_CONDITION_PARAM, ValueSourceConfiguration.forValue(true));
			config.parameters().addSource(OVERWRITE_PARAM, ValueSourceConfiguration.forValue(false));
			return config;
			}

		@Override
		public ResourceDescriptor getDescriptor()
			{
			return new DefaultResourceDescriptor(this, "Injects the task default variables into the context.");
			}

		@SuppressWarnings("WeakerAccess")  // instantiated by reflection
		public TaskDefaultsInitializerType()
			{
			super(TYPE_ID, "Task Defaults Initializer", TaskDefaultsInitializerConfiguration.class, new PluginConfigurationResourceType());
			}
		}
	}
