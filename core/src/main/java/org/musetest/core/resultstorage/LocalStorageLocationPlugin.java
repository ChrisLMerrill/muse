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
        String test_folder = null;
        if (context instanceof TestExecutionContext)
            test_folder = getTestFolder((TestExecutionContext) context).getAbsolutePath();
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