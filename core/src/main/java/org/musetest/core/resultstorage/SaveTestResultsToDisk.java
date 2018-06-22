package org.musetest.core.resultstorage;

import org.jetbrains.annotations.*;
import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.datacollection.*;
import org.musetest.core.events.*;
import org.musetest.core.plugins.*;
import org.musetest.core.test.*;
import org.slf4j.*;

import java.io.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class SaveTestResultsToDisk extends GenericConfigurableTestPlugin implements Shuttable
	{
	SaveTestResultsToDisk(SaveTestResultsToDiskConfiguration configuration)
		{
		super(configuration);
		}

	@Override
	public void initialize(MuseExecutionContext context)
		{
		_location_provider = Plugins.findType(LocalStorageLocationProvider.class, context);
		if (_location_provider == null)
			{
			final String message = "No plugin was found providing LocalStorageLocationProvider services. Don't know where to store results to disk. Results will NOT be saved.";
			context.raiseEvent(MessageEventType.create(message));
			LOG.error(message);
			return;
			}
		context.registerShuttable(this);
		_context = context;
		}

	/**
	 * Do the saving in here, instead of as a END_TEST event listener...because the Collectors may not have received all events, yet.
	 */
	@Override
	public void shutdown()
		{
		File output_folder;
		if (_context instanceof TestExecutionContext)
			output_folder = _location_provider.getTestFolder((TestExecutionContext) _context);
		else
			output_folder = _location_provider.getBaseFolder();
		for (DataCollector collector : DataCollectors.list(_context))
			{
			for (TestResultData data : collector.getData())
				{
				final File data_file = getResultFile(output_folder, data);
				try (FileOutputStream outstream = new FileOutputStream(data_file))
					{
					data.write(outstream);
					}
				catch (IOException e)
					{
					LOG.error(String.format("Unable to store results of test in %s due to: %s", data_file.getAbsolutePath(), e.getMessage()));
					}
				}
			}
		}

	@NotNull
	private File getResultFile(File output_folder, TestResultData data)
		{
		File file = new File(output_folder, data.suggestFilename());
		int index = 2;
		while (file.exists())
			file = new File(output_folder, createIndexedFilename(data.suggestFilename(), index));
		return file;
		}

	private String createIndexedFilename(String starting_name, int index)
		{
		int period = starting_name.lastIndexOf('.');
		return starting_name.substring(0, period) + index + starting_name.substring(period, starting_name.length());
		}

	private MuseExecutionContext _context;
	private LocalStorageLocationProvider _location_provider;

	public final static String TYPE_ID = "save-testresult-to-disk";

	public final static String OUTPUT_FOLDER_VARIABLE_NAME = "__output_folder__";

	private final static Logger LOG = LoggerFactory.getLogger(SaveTestResultsToDisk.class);
	}