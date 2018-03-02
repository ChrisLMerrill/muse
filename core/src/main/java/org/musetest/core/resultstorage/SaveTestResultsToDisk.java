package org.musetest.core.resultstorage;

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
		File output_folder = _location_provider.getTestFolder(MuseExecutionContext.findAncestor(_context, TestExecutionContext.class).getTest());
		for (DataCollector collector : DataCollectors.list(_context))
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

	private MuseExecutionContext _context;
	private LocalStorageLocationProvider _location_provider;

	public final static String TYPE_ID = "save-testresult-to-disk";

	public final static String OUTPUT_FOLDER_VARIABLE_NAME = "__output_folder__";

	private final static Logger LOG = LoggerFactory.getLogger(SaveTestResultsToDisk.class);
	}