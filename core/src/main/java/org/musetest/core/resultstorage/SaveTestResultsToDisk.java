package org.musetest.core.resultstorage;

import org.musetest.core.*;
import org.musetest.core.datacollection.*;
import org.musetest.core.events.*;
import org.musetest.core.test.*;
import org.musetest.core.test.plugins.*;
import org.slf4j.*;

import javax.annotation.*;
import java.io.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class SaveTestResultsToDisk implements TestPlugin, Shuttable
	{
	@Override
	public String getType()
		{
		return TYPE_ID;
		}

	@Override
	public void initialize(MuseExecutionContext context)
		{
		context.registerShuttable(this);
		_context = context;
		}

	/**
	 * Do the saving in here, instead of as a END_TEST event listener...because the EventLog may not have received all events, yet.
	 */
	@Override
	public void shutdown()
		{
		final Object output_folder_value = _context.getVariable(OUTPUT_FOLDER_VARIABLE_NAME);
		if (output_folder_value == null)
			{
			_context.raiseEvent(MessageEventType.create(String.format("Name of output folder was not provided (in variable %s). Results will not be stored.", OUTPUT_FOLDER_VARIABLE_NAME)));
			return;
			}
		String output_folder_path = output_folder_value.toString();
		File output_folder = new File(output_folder_path);
		if (!output_folder.exists())
			if (!output_folder.mkdirs())
				{
				_context.raiseEvent(MessageEventType.create(String.format("Unable to create output folder (%s). Results will not be stored.", output_folder_path)));
				return;
				}
		for (DataCollector collector : _context.getDataCollectors())
			{
			final TestResultData collected = collector.getData();
			if (collected != null)
				{
				final File data_file = new File(output_folder, collected.suggestFilename());
				try (FileOutputStream outstream = new FileOutputStream(data_file))
					{
					collected.write(outstream);
					}
				catch (IOException e)
					{
					LOG.error(String.format("Unable to store results of test in %s due to: %s", data_file.getAbsolutePath(), e.getMessage()));
					}
				}
			}
		}

	@Override
	public void configure(@Nonnull TestPluginConfiguration configuration)
		{

		}

	private MuseExecutionContext _context;

	public final static String TYPE_ID = "save-testresult-to-disk";

	@SuppressWarnings("unused") // used by reflection
	public static class SaveTestResultsToDiskType extends TestPluginType
		{
		@Override
		public String getTypeId()
			{
			return TYPE_ID;
			}

		@Override
		public String getDisplayName()
			{
			return "Save results to disk";
			}

		@Override
		public String getShortDescription()
			{
			return "Save all test result data to files in a folder";
			}
		}

	public final static String OUTPUT_FOLDER_VARIABLE_NAME = "__output_folder__";

	private final static Logger LOG = LoggerFactory.getLogger(SaveTestResultsToDisk.class);
	}