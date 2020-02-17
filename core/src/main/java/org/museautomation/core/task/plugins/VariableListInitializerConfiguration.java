package org.museautomation.core.task.plugins;

import org.museautomation.core.*;
import org.museautomation.core.plugins.*;
import org.museautomation.core.resource.generic.*;
import org.museautomation.core.resource.types.*;
import org.museautomation.core.values.*;
import org.museautomation.core.values.descriptor.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("variable-list-initializer")
@MuseSubsourceDescriptor(displayName = "Apply automatically?", description = "If this source resolves to true, this plugin configuration will be automatically applied to tests", type = SubsourceDescriptor.Type.Named, name = GenericConfigurablePlugin.AUTO_APPLY_PARAM)
@MuseSubsourceDescriptor(displayName = "Apply only if", description = "Apply only if this source this source resolves to true", type = SubsourceDescriptor.Type.Named, name = GenericConfigurablePlugin.APPLY_CONDITION_PARAM)
@MuseSubsourceDescriptor(displayName = "List id", description = "The id of the list containing variables to inject into the context", type = SubsourceDescriptor.Type.Named, name = VariableListInitializerConfiguration.LIST_ID_PARAM)
public class VariableListInitializerConfiguration extends GenericResourceConfiguration implements PluginConfiguration
	{
	@Override
	public ResourceType getType()
		{
		return new VariableListInitializerType();
		}

	@Override
	public VariableListInitializer createPlugin()
		{
		return new VariableListInitializer(this);
		}

	public final static String TYPE_ID = VariableListInitializerConfiguration.class.getAnnotation(MuseTypeId.class).value();
	public final static String LIST_ID_PARAM = "listid";

	@SuppressWarnings("unused") // used by reflection
	public static class VariableListInitializerType extends ResourceSubtype
		{
		@Override
		public MuseResource create()
			{
			final VariableListInitializerConfiguration config = new VariableListInitializerConfiguration();
			config.parameters().addSource(GenericConfigurablePlugin.AUTO_APPLY_PARAM, ValueSourceConfiguration.forValue(true));
			config.parameters().addSource(GenericConfigurablePlugin.APPLY_CONDITION_PARAM, ValueSourceConfiguration.forValue(true));
			config.parameters().addSource(LIST_ID_PARAM, ValueSourceConfiguration.forValue("list1"));
			return config;
			}

		@Override
		public ResourceDescriptor getDescriptor()
			{
			return new DefaultResourceDescriptor(this, "Injects variables from a variable list into the execution context");
			}

		@SuppressWarnings("WeakerAccess")  // instantiated by reflection
		public VariableListInitializerType()
			{
			super(TYPE_ID, "Variable List Initializer", VariableListInitializerConfiguration.class, new PluginConfigurationResourceType());
			}
		}
	}
