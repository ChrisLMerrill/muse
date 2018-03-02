package org.musetest.core.resultstorage;

import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.events.*;
import org.musetest.core.plugins.*;
import org.musetest.core.resource.*;
import org.musetest.core.suite.*;
import org.musetest.core.values.*;

import java.io.*;
import java.util.*;

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
				context.raiseEvent(MessageEventType.create(String.format("Unable to create output folder (%s). Results will not be stored.", output_folder_path)));
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
	synchronized public File getTestFolder(MuseTest test)
		{
		String base_name = test.getId();
		int index = 0;
		if (_test_folder_counts.get(base_name) == null)
			_test_folder_counts.put(base_name, 0);
		else
			{
			index = _test_folder_counts.get(base_name) + 1;
			_test_folder_counts.put(base_name, index);
			}

		final File folder = new File(_output_folder, base_name + "." + index);
		if (!folder.mkdir())
			_context.raiseEvent(MessageEventType.create(String.format("Unable to create output folder (%s). Results will not be stored.", folder.getAbsolutePath())));
		return folder;
		}

	private MuseExecutionContext _context;
	private File _output_folder = null;
	private boolean _initialized = false;
	private Map<String, Integer> _test_folder_counts = new HashMap<>();
	}