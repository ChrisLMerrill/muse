package org.museautomation.builtins.plugins.events;

import org.museautomation.core.*;
import org.museautomation.core.context.*;
import org.museautomation.core.events.*;
import org.museautomation.core.plugins.*;
import org.museautomation.builtins.plugins.resultstorage.*;
import org.museautomation.core.suite.*;
import org.museautomation.core.task.*;
import org.slf4j.*;

import java.io.*;

/**
 * Writes events to a log as they are received.
 *
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class EventLogWriterPlugin extends GenericConfigurablePlugin
	{
	@Override
	protected boolean applyToContextType(MuseExecutionContext context)
		{
		return true;
		}

	public EventLogWriterPlugin(EventLogWriterConfiguration configuration)
		{
		super(configuration);
        _config = configuration;
		}

	@Override
	public void initialize(MuseExecutionContext context)
		{
		context.addEventListener(new EventWriterBuilder(context));
		}

    /**
     * Creates the event writer(s). This doesn't happen until the first event is received to guarantee
     * that all plugins (specifically the LocalStorageLocation plugin) have been initialized.
     */
	private class EventWriterBuilder implements MuseEventListener
        {
        EventWriterBuilder(MuseExecutionContext context)
            {
            _context = context;
            }

        @Override
        public void eventRaised(MuseEvent event)
            {
            File folder;
            LocalStorageLocationProvider provider = Plugins.findType(LocalStorageLocationProvider.class, _context);
            if (provider != null)
                {
                if (_context instanceof TaskExecutionContext)
                    folder = provider.getTaskFolder((TaskExecutionContext) _context);
                else if (_context instanceof TaskSuiteExecutionContext)
                    folder = provider.getBaseFolder();
                else
                    {
                    LOG.error(String.format("EventWriterBuilder does not recognize execution context type (%s). Don't know where to log events :(", _context));
                    _context.removeEventListener(this);  // now that the EventLogPrinters are setup, this listener is no longer needed
                    return;
                    }
                }
            else
                {
                folder = new File(".");
                LOG.info("EventWriterBuilder did not find a LocalStorageLocationProvider. Writing files to current directory: " + folder.getAbsolutePath());
                }

            if (_config.isLogToJson(_context))
                {
                File json_file = new File(folder, PARTIAL_EVENT_FILE);
                try
                    {
                    EventLogJsonPrinter printer = new EventLogJsonPrinter(new PrintStream(new FileOutputStream(json_file)));
                    new EventPrintingListener(_context, printer);
                    }
                catch (FileNotFoundException e)
                    {
                    LOG.error("Unable to write the JSON event log to " + json_file.getAbsolutePath(), e);
                    }
                }

            if (_config.isLogToText(_context))
                {
                File text_file = new File(folder, READABLE_EVENT_FILE);
                try
                    {
                    EventLogPlainTextPrinter printer = new EventLogPlainTextPrinter(new PrintStream(new FileOutputStream(text_file)));
                    new EventPrintingListener(_context, printer);
                    }
                catch (FileNotFoundException e)
                    {
                    LOG.error("Unable to write the plain-text event log to " + text_file.getAbsolutePath(), e);
                    }
                }

            if (_config.isLogToStdout(_context))
                {
                EventLogPlainTextPrinter printer = new EventLogPlainTextPrinter(System.out);
                new EventPrintingListener(_context, printer);
                }

            _context.removeEventListener(this);  // now that the EventLogPrinters are setup, this listener is no longer needed
            }

        private final MuseExecutionContext _context;
        }

	static class EventPrintingListener implements MuseEventListener, Shuttable
        {
        EventPrintingListener(MuseExecutionContext context, EventLogPrinter printer)
            {
            _context = context;
            _context.addEventListener(this);
            _context.registerShuttable(this);
            _printer = printer;

            // print all existing events, plus the supplied event
            for (MuseEvent e : context.getEventLog().getEvents())
                eventRaised(e);
            }

        @Override
        public void eventRaised(MuseEvent event)
            {
            try
                {
                _printer.print(event);
                }
            catch (IOException e)
                {
                LOG.error("Unable to write the event to (%s) using the EventLogPrinter (%s)", e);
                _context.removeEventListener(this);
                }
            }

        @Override
        public void shutdown()
            {
            _printer.finish();
            }

        MuseExecutionContext _context;
        EventLogPrinter _printer;
        }

    private final EventLogWriterConfiguration _config;

	public final static String TYPE_ID = "event-logger";
	final static String PARTIAL_EVENT_FILE = "events.json";
	final static String READABLE_EVENT_FILE = "events.txt";

	private final static Logger LOG = LoggerFactory.getLogger(EventLogWriterPlugin.class);
	}