package org.museautomation.builtins.plugins.output;

import org.museautomation.core.*;
import org.museautomation.core.plugins.*;
import org.museautomation.core.resource.generic.*;
import org.museautomation.core.resource.types.*;
import org.museautomation.core.values.*;
import org.museautomation.core.values.descriptor.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("pass-fail-suite-csv-output-writer-plugin")
@MuseSubsourceDescriptor(displayName = "Apply automatically?", description = "If this source resolves to true, this plugin configuration will be automatically applied to tests", type = SubsourceDescriptor.Type.Named, name = GenericConfigurablePlugin.AUTO_APPLY_PARAM)
@MuseSubsourceDescriptor(displayName = "Apply only if", description = "Apply only if this source this source resolves to true", type = SubsourceDescriptor.Type.Named, name = GenericConfigurablePlugin.APPLY_CONDITION_PARAM)
@MuseSubsourceDescriptor(displayName = "Output filename", description = "Name of file to write to", type = SubsourceDescriptor.Type.Named, name = PassFailSuiteCsvOutputWriterConfiguration.FILENAME_PARAM, defaultValue = "output")
@MuseSubsourceDescriptor(displayName = "Variable Names", description = "A list of names of variables to collect", type = SubsourceDescriptor.Type.Named, name = PassFailSuiteCsvOutputWriterConfiguration.VARIABLE_NAMES_LIST_PARAM, defaultValue = "[\"var1\",\"var2\"]")
@MuseSubsourceDescriptor(displayName = "From Successful", description = "Collect from tasks that complete successfully", type = SubsourceDescriptor.Type.Named, name = PassFailSuiteCsvOutputWriterConfiguration.SUCCESSFUL_TASKS_PARAM, optional = true, defaultValue = "true")
@MuseSubsourceDescriptor(displayName = "From Failed", description = "Collect from tasks that complete with failures", type = SubsourceDescriptor.Type.Named, name = PassFailSuiteCsvOutputWriterConfiguration.FAILED_TASKS_PARAM, optional = true, defaultValue = "true")
@SuppressWarnings("unused") // Instantiated by reflection
public class PassFailSuiteCsvOutputWriterConfiguration extends GenericResourceConfiguration implements PluginConfiguration
	{
	@Override
	public PassFailSuiteCsvOutputWriter createPlugin()
		{
		return new PassFailSuiteCsvOutputWriter(this);
		}

	@Override
	public ResourceType getType()
		{
		return new PassFailSuiteCsvOutputWriterConfigurationType();
		}

	public final static String TYPE_ID = PassFailSuiteCsvOutputWriterConfiguration.class.getAnnotation(MuseTypeId.class).value();

	@SuppressWarnings("unused") // used by reflection
	public static class PassFailSuiteCsvOutputWriterConfigurationType extends ResourceSubtype
		{
		@Override
		public PassFailSuiteCsvOutputWriterConfiguration create()
			{
			final PassFailSuiteCsvOutputWriterConfiguration config = new PassFailSuiteCsvOutputWriterConfiguration();
			config.parameters().addSource(GenericConfigurablePlugin.AUTO_APPLY_PARAM, ValueSourceConfiguration.forValue(true));
			config.parameters().addSource(GenericConfigurablePlugin.APPLY_CONDITION_PARAM, ValueSourceConfiguration.forValue(true));
			config.parameters().addSource(FILENAME_PARAM, ValueSourceConfiguration.forValue("output"));
			config.parameters().addSource(VARIABLE_NAMES_LIST_PARAM, ValueSourceConfiguration.forValue(Arrays.asList("varname1","varname2")));
			config.parameters().addSource(SUCCESSFUL_TASKS_PARAM, ValueSourceConfiguration.forValue(true));
			return config;
			}

		@SuppressWarnings("WeakerAccess")  // instantiated by reflection
		public PassFailSuiteCsvOutputWriterConfigurationType()
			{
			super(TYPE_ID, "Pass/Fail Suite Csv Output Writer", PassFailSuiteCsvOutputWriterConfiguration.class, new PluginConfigurationResourceType());
			}
		}

    public final static String FILENAME_PARAM = "filename";
    public final static String VARIABLE_NAMES_LIST_PARAM = "variable-names";
    public final static String SUCCESSFUL_TASKS_PARAM = "successful-tasks";
    public final static String FAILED_TASKS_PARAM = "failed-tasks";
	}