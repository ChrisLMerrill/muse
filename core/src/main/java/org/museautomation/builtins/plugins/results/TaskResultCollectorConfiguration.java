package org.museautomation.builtins.plugins.results;

import org.museautomation.core.*;
import org.museautomation.core.plugins.*;
import org.museautomation.core.resource.generic.*;
import org.museautomation.core.resource.types.*;
import org.museautomation.core.values.*;
import org.museautomation.core.values.descriptor.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("task-result-collector")
@MuseSubsourceDescriptor(displayName = "Apply automatically?", description = "If this source resolves to true, this plugin configuration will be automatically applied to takss", type = SubsourceDescriptor.Type.Named, name = GenericConfigurablePlugin.AUTO_APPLY_PARAM)
@MuseSubsourceDescriptor(displayName = "Apply only if", description = "Apply only if this source this source resolves to true", type = SubsourceDescriptor.Type.Named, name = GenericConfigurablePlugin.APPLY_CONDITION_PARAM)
@MuseSubsourceDescriptor(displayName = "Fail on failure", description = "Task fails if any step reports failure (default is true)", type = SubsourceDescriptor.Type.Named, name = TaskResultCollectorConfiguration.FAIL_ON_FAILURE, optional = true)
@MuseSubsourceDescriptor(displayName = "Fail on error", description = "Task fails if any step reports an error (default is true)", type = SubsourceDescriptor.Type.Named, name = TaskResultCollectorConfiguration.FAIL_ON_ERROR, optional = true)
@MuseSubsourceDescriptor(displayName = "Fail on interrupt", description = "Task fails if the task is interrupted (default is true)", type = SubsourceDescriptor.Type.Named, name = TaskResultCollectorConfiguration.FAIL_ON_INTERRUPT, optional = true)
// instantiated by reflection
public class TaskResultCollectorConfiguration extends GenericResourceConfiguration implements PluginConfiguration
	{
	@Override
	public ResourceType getType()
		{
		return new TaskResultCollectorConfigurationType();
		}

	@Override
	public MusePlugin createPlugin()
		{
		return new TaskResultCollector(this);
		}

	public final static String TYPE_ID = TaskResultCollectorConfiguration.class.getAnnotation(MuseTypeId.class).value();
	public final static String FAIL_ON_INTERRUPT = "fail-on-interrupt";
	public final static String FAIL_ON_FAILURE = "fail-on-failure";
	public final static String FAIL_ON_ERROR = "fail-on-error";

	public static class TaskResultCollectorConfigurationType extends ResourceSubtype
		{
		@Override
		public TaskResultCollectorConfiguration create()
			{
			final TaskResultCollectorConfiguration config = new TaskResultCollectorConfiguration();
			config.parameters().addSource(GenericConfigurablePlugin.AUTO_APPLY_PARAM, ValueSourceConfiguration.forValue(true));
			config.parameters().addSource(GenericConfigurablePlugin.APPLY_CONDITION_PARAM, ValueSourceConfiguration.forValue(true));
			config.parameters().addSource(FAIL_ON_ERROR, ValueSourceConfiguration.forValue(true));
			config.parameters().addSource(FAIL_ON_FAILURE, ValueSourceConfiguration.forValue(true));
			config.parameters().addSource(FAIL_ON_INTERRUPT, ValueSourceConfiguration.forValue(true));
			return config;
			}

        @Override
      		public ResourceDescriptor getDescriptor()
      			{
      			return new DefaultResourceDescriptor(this, "Determines the result of a task.");
      			}

		// instantiated by reflection
		public TaskResultCollectorConfigurationType()
			{
			super(TYPE_ID, "Task Result Calculator", TaskResultCollector.class, new PluginConfigurationResourceType());
			}
		}
	}
