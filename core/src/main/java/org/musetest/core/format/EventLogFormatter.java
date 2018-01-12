package org.musetest.core.format;

import org.musetest.core.*;
import org.musetest.core.events.*;
import org.musetest.core.resource.json.*;
import org.musetest.core.util.*;
import org.slf4j.*;

import java.io.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@SuppressWarnings("unused")  // invoked via reflection
public class EventLogFormatter implements Reformatter
	{
	@Override
	public boolean canReformat(MuseProject project, Object object, String format)
		{
		return object instanceof EventLog && format.equals("txt");
		}

	@Override
	public boolean canReformat(MuseProject project, File file, String type, String format)
		{
		return type.equals("EventLog") && format.equals("txt");
		}

	@Override
	public void reformat(MuseProject project, Object object, String format, OutputStream outstream)
		{
		}

	@Override
	public void reformat(MuseProject project, File file, String type, String format, OutputStream outstream)
		{
		try (FileInputStream instream = new FileInputStream(file))
			{
			EventLog log = JsonResourceSerializer.getMapper(new TypeLocator(project.getClassLocator())).readValue(instream, EventLog.class);
			write(log, outstream);
			}
		catch (IOException e)
			{
			LOG.error("Failed to read EventLog", e);
			}
		}

	private void write(EventLog log, OutputStream outstream)
		{
		try
			{
			PrintStream printstream;
			if (outstream instanceof PrintStream)
				printstream = (PrintStream) outstream;
			else
				printstream = new PrintStream(outstream);
			EventLogPrinter printer = new EventLogPrinter();
			printer.setOutput(printstream);
			for (MuseEvent event : log.getEvents())
				printer.print(event);
			}
		catch (Exception e)
			{
			LOG.error("Failed to write EventLog", e);
			}
		}

	private final static Logger LOG = LoggerFactory.getLogger(EventLogFormatter.class);
	}


