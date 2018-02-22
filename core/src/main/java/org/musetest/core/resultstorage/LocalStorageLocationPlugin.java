package org.musetest.core.resultstorage;

import org.musetest.core.*;
import org.musetest.core.datacollection.*;
import org.musetest.core.events.*;
import org.musetest.core.plugins.*;
import org.musetest.core.test.*;
import org.slf4j.*;

import java.io.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class LocalStorageLocationPlugin extends GenericConfigurableTestPlugin implements LocalStorageLocationProvider
	{
	LocalStorageLocationPlugin(LocalStorageLocationPluginConfiguration configuration)
		{
		super(configuration);
		}

	@Override
	public void initialize(MuseExecutionContext context)
		{
		final Object output_folder_value = context.getVariable(SaveTestResultsToDisk.OUTPUT_FOLDER_VARIABLE_NAME);
		if (output_folder_value == null)
			{
			context.raiseEvent(MessageEventType.create(String.format("Name of output folder was not provided (in variable %s). Results will not be stored.", SaveTestResultsToDisk.OUTPUT_FOLDER_VARIABLE_NAME)));
			return;
			}
		String output_folder_path = output_folder_value.toString();
		_output_folder = new File(output_folder_path);
		if (!_output_folder.exists())
			if (!_output_folder.mkdirs())
				context.raiseEvent(MessageEventType.create(String.format("Unable to create output folder (%s). Results will not be stored.", output_folder_path)));

		// TODO verify we can write to that folder
		}

	@Override
	public File getBaseFolder()
		{
		return _output_folder;
		}

	private File _output_folder = null;
	}