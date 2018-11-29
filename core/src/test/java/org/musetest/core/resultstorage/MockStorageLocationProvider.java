package org.musetest.core.resultstorage;

import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.plugins.*;

import java.io.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
class MockStorageLocationProvider implements MusePlugin, LocalStorageLocationProvider
	{
	MockStorageLocationProvider(File base_folder, File test_folder)
		{
		_base_folder = base_folder;
		_test_folder = test_folder;
		}

	@Override
	public boolean conditionallyAddToContext(MuseExecutionContext context, boolean automatic)
		{
		return true;
		}

	@Override
	public void initialize(MuseExecutionContext context)
		{

		}

	@Override
	public void shutdown()
		{

		}

	@Override
	public String getId()
		{
		return "MockStorageLocationProvider";
		}

	@Override
	public File getBaseFolder()
		{
		return _base_folder;
		}

	@Override
	public File getTestFolder(TestExecutionContext test_context)
		{
		return _test_folder;
		}

	private final File _base_folder;
	private final File _test_folder;
	}


