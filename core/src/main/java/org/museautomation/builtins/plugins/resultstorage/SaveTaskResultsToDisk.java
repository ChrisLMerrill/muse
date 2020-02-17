package org.museautomation.builtins.plugins.resultstorage;

import org.jetbrains.annotations.*;
import org.museautomation.core.*;
import org.museautomation.core.context.*;
import org.museautomation.core.datacollection.*;
import org.museautomation.core.events.*;
import org.museautomation.core.plugins.*;
import org.museautomation.core.suite.*;
import org.slf4j.*;

import java.io.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class SaveTaskResultsToDisk extends GenericConfigurablePlugin
	{
	SaveTaskResultsToDisk(SaveTaskResultsToDiskConfiguration configuration)
		{
		super(configuration);
		_configuration = configuration;
		}

	@Override
	protected boolean applyToContextType(MuseExecutionContext context)
		{
		return context instanceof TaskSuiteExecutionContext || context instanceof TaskExecutionContext;
		}

	@Override
	public void initialize(MuseExecutionContext context)
		{
		_save_at_end = _configuration.isSaveResultsAtEnd(context);
		boolean save_immediate = _configuration.isSaveResultsImmmediately(context);
		_delete_data = _configuration.isDeleteDataAfterImmedateSave(context);

		_location_provider = Plugins.findType(LocalStorageLocationProvider.class, context);
		if (_location_provider == null)
			{
			final String message = "No plugin was found providing LocalStorageLocationProvider services. Don't know where to store results to disk. Results will NOT be saved.";
			context.raiseEvent(MessageEventType.create(message));
			LOG.error(message);
			return;
			}
		if (save_immediate)
			context.addEventListener(event ->
				{
				if (TaskResultStoredEventType.TYPE_ID.equals(event.getTypeId()))
					{
					final String var_name = event.getAttributeAsString(TaskResultStoredEventType.VARIABLE_NAME);
					Object data = _context.getVariable(var_name);
					if (data instanceof TaskResultData)
						{
						if (storeResult((TaskResultData)data, "$" + var_name) && _delete_data)
							_context.setVariable(var_name, null);
						}
					else
						LOG.error(String.format("Unable to save task result because $%s is not a TaskResultData. It is a %s", var_name, data.getClass().getSimpleName()));
					}
				});
		_context = context;
		}

	/**
	 * Do the saving in here, instead of as a END_TASK event listener...because the Collectors may not have received all events, yet.
	 */
	@Override
	public void shutdown()
		{
		if (_save_at_end)
			for (DataCollector collector : DataCollectors.list(_context))
				{
				for (TaskResultData data : collector.getData())
					storeResult(data, collector.getClass().getSimpleName());
				}
		}

	private boolean storeResult(TaskResultData data, String from_description)
		{
		final File data_file = getResultFile(getOutputFolder(), data);
		try (FileOutputStream outstream = new FileOutputStream(data_file))
			{
			data.write(outstream);
			_context.raiseEvent(MessageEventType.create(String.format("Stored %s from %s to %s", data, from_description, data_file.getAbsolutePath())));
			return true;
			}
		catch (IOException e)
			{
			_context.raiseEvent(MessageEventType.create(String.format("Unable to store %s from %s to %s because: %s", data, from_description, data_file.getAbsolutePath(), e.getMessage())));
			LOG.error(String.format("Unable to store results of task in %s due to: %s", data_file.getAbsolutePath(), e.getMessage()));
			return false;
			}
		}

	private File getOutputFolder()
		{
		if (_output_folder == null)
			{
			if (_context instanceof TaskExecutionContext)
				_output_folder = _location_provider.getTaskFolder((TaskExecutionContext) _context);
			else
				_output_folder = _location_provider.getBaseFolder();
			}
		return _output_folder;
		}

	@NotNull
	private File getResultFile(File output_folder, TaskResultData data)
		{
		File file = new File(output_folder, data.suggestFilename());
		int index = 2;
		while (file.exists())
			file = new File(output_folder, createIndexedFilename(data.suggestFilename(), index++));
		return file;
		}

	private String createIndexedFilename(String starting_name, int index)
		{
		int period = starting_name.lastIndexOf('.');
		return starting_name.substring(0, period) + index + starting_name.substring(period);
		}

	private SaveTaskResultsToDiskConfiguration _configuration;
	private MuseExecutionContext _context;
	private LocalStorageLocationProvider _location_provider;
	private File _output_folder = null;
	private boolean _save_at_end = true;
	private boolean _delete_data = true;

	public final static String OUTPUT_FOLDER_VARIABLE_NAME = "__output_folder__";

	private final static Logger LOG = LoggerFactory.getLogger(SaveTaskResultsToDisk.class);
	}