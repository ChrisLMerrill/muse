package org.museautomation.core.suite.plugins;

import org.museautomation.core.*;
import org.museautomation.core.plugins.*;
import org.museautomation.core.resource.generic.*;
import org.museautomation.core.resource.types.*;
import org.museautomation.core.values.*;
import org.museautomation.core.values.descriptor.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("task-suite-result-counter")
@MuseSubsourceDescriptor(displayName = "Apply automatically?", description = "If this source resolves to true, this plugin configuration will be automatically applied to tests", type = SubsourceDescriptor.Type.Named, name = GenericConfigurablePlugin.AUTO_APPLY_PARAM)
@MuseSubsourceDescriptor(displayName = "Apply only if", description = "Apply only if this source this source resolves to true", type = SubsourceDescriptor.Type.Named, name = GenericConfigurablePlugin.APPLY_CONDITION_PARAM)
@SuppressWarnings("unused") // used by reflection
public class TaskSuiteResultCounterConfiguration extends GenericResourceConfiguration implements PluginConfiguration
	{
	@Override
	public TaskSuiteResultCounter createPlugin()
		{
		return new TaskSuiteResultCounter(this);
		}

	@Override
	public ResourceType getType()
		{
		return new TaskSuiteResultCounterType();
		}

	public final static String TYPE_ID = TaskSuiteResultCounterConfiguration.class.getAnnotation(MuseTypeId.class).value();

	@SuppressWarnings("unused") // used by reflection
	public static class TaskSuiteResultCounterType extends ResourceSubtype
		{
		@Override
		public TaskSuiteResultCounterConfiguration create()
			{
			final TaskSuiteResultCounterConfiguration config = new TaskSuiteResultCounterConfiguration();
			config.parameters().addSource(GenericConfigurablePlugin.AUTO_APPLY_PARAM, ValueSourceConfiguration.forValue(true));
			config.parameters().addSource(GenericConfigurablePlugin.APPLY_CONDITION_PARAM, ValueSourceConfiguration.forValue(true));
			return config;
			}

		@Override
		public ResourceDescriptor getDescriptor()
			{
			return new DefaultResourceDescriptor(this, "Summarize the results of all tasks in the suite");
			}

		@SuppressWarnings("WeakerAccess")  // instantiated by reflection
		public TaskSuiteResultCounterType()
			{
			super(TYPE_ID, "Test Suite Result Counter", TaskSuiteResultCounter.class, new PluginConfigurationResourceType());
			}
		}
	}