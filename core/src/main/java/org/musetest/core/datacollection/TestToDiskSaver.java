package org.musetest.core.datacollection;

import org.musetest.core.*;
import org.musetest.core.suite.*;
import org.slf4j.*;

import java.io.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class TestToDiskSaver implements TestResultDataSaver
	{
	public TestToDiskSaver(File folder)
		{
		_folder = folder;
		}

	@Override
	public boolean save(MuseProject project, MuseTestResult result, TestConfiguration test, MuseExecutionContext context)
		{
		String test_name = test.getTest().getId();
		File base_file = new File(_folder, test_name);
		if (!base_file.mkdir())
			{
			LOG.error("Unable to created folder to save result");
			return false;
			}
		for (DataCollector collector : context.getDataCollectors())
			{
			final File data_file = new File(base_file, collector.getData().suggestFilename());
			try (FileOutputStream outstream = new FileOutputStream(data_file))
				{
				collector.getData().write(outstream);
				}
			catch (IOException e)
				{
				LOG.error(String.format("Unable to store results of test %s in %s due to: %s", test_name, data_file.getAbsolutePath(), e.getMessage()));
				}
			}
		return true;
		}

	private final File _folder;

	private final static Logger LOG = LoggerFactory.getLogger(TestToDiskSaver.class);
	}