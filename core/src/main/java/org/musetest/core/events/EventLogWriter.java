package org.musetest.core.events;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.jetbrains.annotations.*;
import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.datacollection.*;
import org.musetest.core.plugins.*;
import org.musetest.core.resource.generic.*;
import org.musetest.core.resultstorage.*;
import org.slf4j.*;

import java.io.*;
import java.util.*;

/**
 * Writes events to a log as they are received.
 *
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class EventLogWriter extends GenericConfigurablePlugin implements MuseEventListener, DataCollector
	{
	public EventLogWriter()
		{
		super(new EventLogWriterConfiguration.EventLogWriterType().create());
		}

	@Override
	protected boolean applyToContextType(MuseExecutionContext context)
		{
		return true;
		}

	EventLogWriter(GenericResourceConfiguration configuration)
		{
		super(configuration);
		}

	@Override
	public void initialize(MuseExecutionContext context)
		{
		context.addEventListener(this);
		_context = context;
		}

	@Override
	public void eventRaised(MuseEvent event)
		{
		_log.add(event);

		if (StartTestEventType.TYPE_ID.equals(event.getTypeId())
			|| StartSuiteEventType.TYPE_ID.equals(event.getTypeId()))
			flushWaitingEvents();

		if (_unwritten_events != null)
			/*
			 * The output location cannot be determined until all plugins have been initialized. Until then, events
			 * will be held for writing when the test/suite starts.
			 */
			_unwritten_events.add(event);
		else
			{
			try
				{
				writeEvent(event);
				}
			catch (IOException e)
				{
				LOG.error("Unable to write to event log", e);
				}
			}
		}

	private void writeEvent(MuseEvent event) throws IOException
		{
		OutputStream outstream = getOutputStream();
		if (_events_written > 0)
			{
			outstream.write(',');
			outstream.write('\n');
			}
		getMapper().writerWithDefaultPrettyPrinter().writeValue(outstream, event);
		outstream.flush();
		_events_written++;
		if (_printer != null)
			_printer.print(event);
		}

	private void flushWaitingEvents()
		{
		if (_unwritten_events != null)
			for (MuseEvent event : _unwritten_events)
				try
					{
					writeEvent(event);
					}
				catch (IOException e)
					{
					LOG.error("Unable to write event", e);
					}
		_unwritten_events = null;
		}

	@Override
	@NotNull
	public List<TestResultData> getData()
		{
		return Collections.singletonList(_log);
		}

	@Override
	public void shutdown()
		{
		flushWaitingEvents();
		try
			{
			_outstream.write(']');
			_outstream.close();
			_print_stream.close();
			}
		catch (IOException e)
			{
			// nothing we can do about this now.
			}
		}

	public EventLog getLog()
		{
		return _log;
		}

	public File getLogfile()
		{
		return _file;
		}

	private OutputStream getOutputStream() throws FileNotFoundException
		{
		if (_outstream == null)
			{
			if (_file == null)
				{
				File folder = null;
				for (MusePlugin plugin : _context.getPlugins())
					if (plugin instanceof LocalStorageLocationProvider)
						{
						if (_context instanceof TestExecutionContext)
							folder = ((LocalStorageLocationProvider)plugin).getTestFolder((TestExecutionContext) _context);
						else
							folder = ((LocalStorageLocationProvider)plugin).getBaseFolder();
						break;
						}

				if (folder == null)
					{
					_file = new File(PARTIAL_EVENT_FILE);
					_print_stream = new PrintStream(new FileOutputStream(new File(READABLE_EVENT_FILE)));
					}
				else
					{
					_file = new File(folder, PARTIAL_EVENT_FILE);
					_print_stream = new PrintStream(new FileOutputStream(new File(folder, READABLE_EVENT_FILE)));
					}
				}
			_outstream = new FileOutputStream(_file);
			try
				{
				_outstream.write('[');
				}
			catch (IOException e)
				{
				LOG.error("Unable to write to log file " + _file.getPath(), e);
				}
			_printer = new EventLogPrinter(_print_stream);
			}
		return _outstream;
		}

	private ObjectMapper getMapper()
		{
		if (_mapper == null)
			{
			_mapper = new ObjectMapper();
			_mapper.disable(JsonGenerator.Feature.AUTO_CLOSE_TARGET);
			}
		return _mapper;
		}

	/**
	 * Set the file to write log events to. This should be handled automatically, so this is for unit testing.
	 */
	void setLogFile(File file) throws FileNotFoundException
		{
		_file = file;
		_outstream = new FileOutputStream(_file);
		}

	private MuseExecutionContext _context;
	private EventLog _log = new EventLog();
	private File _file = null;
	private OutputStream _outstream = null;
	private EventLogPrinter _printer = null;
	private PrintStream  _print_stream = null;
	private ObjectMapper _mapper = null;
	private List<MuseEvent> _unwritten_events = new ArrayList<>();
	private int _events_written = 0;


	public final static String TYPE_ID = "event-logger";
	private final static String PARTIAL_EVENT_FILE = "events.json";
	private final static String READABLE_EVENT_FILE = "events.txt";

	private final static Logger LOG = LoggerFactory.getLogger(EventLogWriter.class);
	}