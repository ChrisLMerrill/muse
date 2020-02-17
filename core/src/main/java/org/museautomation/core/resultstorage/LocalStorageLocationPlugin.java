package org.museautomation.core.resultstorage;

import org.museautomation.core.*;
import org.museautomation.core.context.*;
import org.museautomation.core.events.*;
import org.museautomation.core.plugins.*;
import org.museautomation.core.resource.*;
import org.museautomation.core.suite.*;
import org.museautomation.core.values.*;

import java.io.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class LocalStorageLocationPlugin extends GenericConfigurableTaskPlugin implements LocalStorageLocationProvider
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
        String test_folder = null;
        if (context instanceof TaskExecutionContext)
            test_folder = getTaskFolder((TaskExecutionContext) context).getAbsolutePath();
		if (!_output_folder.exists())
			if (!_output_folder.mkdirs())
				{
				context.raiseEvent(LocalStorageLocationEventType.create(_output_folder.getAbsolutePath(), test_folder, "Unable to create output folder. Results will not be stored."));
				_output_folder = null;
				}
		if (_output_folder != null)
			context.raiseEvent(LocalStorageLocationEventType.create(_output_folder.getAbsolutePath(), test_folder, null));
		}

	@Override
	protected boolean applyToContextType(MuseExecutionContext context)
		{
		if (Plugins.findType(this.getClass(), context) != null)
			return false;

		return context instanceof TaskSuiteExecutionContext || context instanceof TaskExecutionContext;
		}

	@Override
	public File getBaseFolder()
		{
		return _output_folder;
		}

	@Override
	synchronized public File getTaskFolder(TaskExecutionContext task_context)
		{
		final File folder = new File(_output_folder, task_context.getTaskExecutionId());
		if (!folder.exists())
			if (!folder.mkdir())
				_context.raiseEvent(MessageEventType.create(String.format("Unable to create output folder (%s). Results will not be stored.", folder.getAbsolutePath())));
		return folder;
		}

	private MuseExecutionContext _context;
	private File _output_folder = null;
	private boolean _initialized = false;
	}