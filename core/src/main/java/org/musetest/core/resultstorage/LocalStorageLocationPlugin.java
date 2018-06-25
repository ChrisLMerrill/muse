package org.musetest.core.resultstorage;

import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.events.*;
import org.musetest.core.plugins.*;
import org.musetest.core.resource.*;
import org.musetest.core.suite.*;
import org.musetest.core.values.*;

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
	public void initialize(MuseExecutionContext context) throws MuseInstantiationException, ValueSourceResolutionError
		{
		if (_initialized)
			return;

		_context = context;
		_initialized = true;
		MuseValueSource output_folder_source = BaseValueSource.getValueSource(_configuration.parameters(), LocalStorageLocationPluginConfiguration.BASE_LOCATION_PARAM_NAME, true, context.getProject());
		String output_folder_path = BaseValueSource.getValue(output_folder_source, context, false, String.class);
		_output_folder = new File(output_folder_path);
		if (!_output_folder.exists())
			if (!_output_folder.mkdirs())
				{
				final MuseEvent event = MessageEventType.create(String.format("Unable to create output folder (%s). Results will not be stored.", output_folder_path));
				event.addTag(MuseEvent.ERROR);
				context.raiseEvent(event);
				_output_folder = null;
				}
		if (_output_folder != null)
			context.raiseEvent(MessageEventType.create(String.format("Will store results locally at %s.", _output_folder.getAbsolutePath())));
		}

	@Override
	protected boolean applyToContextType(MuseExecutionContext context)
		{
		if (Plugins.findType(this.getClass(), context) != null)
			return false;

		return context instanceof TestSuiteExecutionContext || context instanceof TestExecutionContext;
		}

	@Override
	public File getBaseFolder()
		{
		return _output_folder;
		}

	@Override
	synchronized public File getTestFolder(TestExecutionContext context)
		{
		final File folder = new File(_output_folder, context.getTestExecutionId());
		if (!folder.exists())
			if (!folder.mkdir())
				_context.raiseEvent(MessageEventType.create(String.format("Unable to create output folder (%s). Results will not be stored.", folder.getAbsolutePath())));
		return folder;
		}

	private MuseExecutionContext _context;
	private File _output_folder = null;
	private boolean _initialized = false;
	}