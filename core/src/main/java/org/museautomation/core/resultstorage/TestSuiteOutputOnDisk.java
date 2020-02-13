package org.museautomation.core.resultstorage;

import org.museautomation.core.test.*;

import java.io.*;
import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class TestSuiteOutputOnDisk
	{
	public TestSuiteOutputOnDisk(String output_path)
		{
		if (output_path == null)
			return;

		_output_folder = new File(output_path);
		if (!_output_folder.exists())
			{
			if (!_output_folder.mkdirs())
				System.out.println("Unable to create output folder: " + output_path);
			}
		}

	public String getOutputFolderName(TestConfiguration configuration)
		{
		String base_name = configuration.test().getId();
		int index = 0;
		if (_output_folders.get(base_name) == null)
			_output_folders.put(base_name, 0);
		else
			{
			index = _output_folders.get(base_name) + 1;
			_output_folders.put(base_name, index);
			}

		return new File(_output_folder, base_name + "." + index).getPath();
		}

	private File _output_folder;
	private Map<String, Integer> _output_folders = new HashMap<>();
	}