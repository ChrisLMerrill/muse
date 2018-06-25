package org.musetest.core.resultstorage;

import org.musetest.core.*;
import org.musetest.core.plugins.*;
import org.musetest.core.resource.generic.*;
import org.musetest.core.resource.types.*;
import org.musetest.core.values.*;
import org.musetest.core.values.descriptor.*;

import static org.musetest.core.resultstorage.SaveTestResultsToDiskConfiguration.DELETE_IMMEDIATE_PARAM;
import static org.musetest.core.resultstorage.SaveTestResultsToDiskConfiguration.SAVE_AT_END_PARAM;
import static org.musetest.core.resultstorage.SaveTestResultsToDiskConfiguration.SAVE_IMMEDIATE_PARAM;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("save-results-to-disk")
@MuseSubsourceDescriptor(displayName = "Apply automatically?", description = "If this source resolves to true, this plugin configuration will be automatically applied to tests", type = SubsourceDescriptor.Type.Named, name = GenericConfigurablePlugin.AUTO_APPLY_PARAM)
@MuseSubsourceDescriptor(displayName = "Apply only if", description = "Apply only if this source this source resolves to true", type = SubsourceDescriptor.Type.Named, name = GenericConfigurablePlugin.APPLY_CONDITION_PARAM)
@MuseSubsourceDescriptor(displayName = "Save results from events", description = "If this source resolves to true, save results immediately in response to a TestResultsStored event.", type = SubsourceDescriptor.Type.Named, name = SAVE_IMMEDIATE_PARAM, optional = true, defaultValue = "true")
@MuseSubsourceDescriptor(displayName = "Delete results from context", description = "If this source resolves to true, delete saved results from the execution context in response to a TestResultsStored event.", type = SubsourceDescriptor.Type.Named, name = DELETE_IMMEDIATE_PARAM, optional = true, defaultValue = "true")
@MuseSubsourceDescriptor(displayName = "Save when test completes", description = "If this source resolves to true, save results from data collectors after the test completes.", type = SubsourceDescriptor.Type.Named, name = SAVE_AT_END_PARAM, optional = true, defaultValue = "true")
@SuppressWarnings("WeakerAccess")  // instantiated by reflection
public class SaveTestResultsToDiskConfiguration extends GenericResourceConfiguration implements PluginConfiguration
	{
	@Override
	public ResourceType getType()
		{
		return new SaveTestResultsToDiskConfigurationType();
		}

	@Override
	public SaveTestResultsToDisk createPlugin()
		{
		return new SaveTestResultsToDisk(this);
		}

	public boolean isSaveResultsImmmediately(MuseExecutionContext context)
		{
		if (_parameters.getSource(SAVE_IMMEDIATE_PARAM) == null)
			return true;
		return isParameterTrue(context, SAVE_IMMEDIATE_PARAM);
		}

	public boolean isSaveResultsAtEnd(MuseExecutionContext context)
		{
		if (_parameters.getSource(SAVE_AT_END_PARAM) == null)
			return true;
		return isParameterTrue(context, SAVE_AT_END_PARAM);
		}

	public boolean isDeleteDataAfterImmedateSave(MuseExecutionContext context)
		{
		if (_parameters.getSource(DELETE_IMMEDIATE_PARAM) == null)
			return true;
		return isParameterTrue(context, DELETE_IMMEDIATE_PARAM);
		}

	public final static String TYPE_ID = SaveTestResultsToDiskConfiguration.class.getAnnotation(MuseTypeId.class).value();

	public static class SaveTestResultsToDiskConfigurationType extends ResourceSubtype
		{
		@Override
		public SaveTestResultsToDiskConfiguration create()
			{
			final SaveTestResultsToDiskConfiguration config = new SaveTestResultsToDiskConfiguration();
			config.parameters().addSource(GenericConfigurablePlugin.AUTO_APPLY_PARAM, ValueSourceConfiguration.forValue(true));
			config.parameters().addSource(GenericConfigurablePlugin.APPLY_CONDITION_PARAM, ValueSourceConfiguration.forValue(true));
			return config;
			}

		@Override
		public ResourceDescriptor getDescriptor()
			{
			return new DefaultResourceDescriptor(this, "Save the test results from all DataCollectors to disk.");
			}

		public SaveTestResultsToDiskConfigurationType()
			{
			super(TYPE_ID, "Save Result to Disk", SaveTestResultsToDiskConfiguration.class, new PluginConfigurationResourceType());
			}
		}

	public final static String SAVE_AT_END_PARAM = "save-at-end";
	public final static String SAVE_IMMEDIATE_PARAM = "save-immediate";
	public final static String DELETE_IMMEDIATE_PARAM = "delete-immediate";
	}


