package org.museautomation.junit;

import com.google.common.html.*;
import org.jetbrains.annotations.*;
import org.museautomation.core.*;
import org.museautomation.core.datacollection.*;
import org.museautomation.core.events.*;

import javax.annotation.*;
import javax.annotation.Nullable;
import java.io.*;
import java.nio.charset.*;
import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class JUnitReportData implements TaskResultData
	{
	@Override
	public String getName()
		{
		return _name;
		}

	@Override
	public void setName(@Nonnull String name)
		{
		_name = name;
		}

	@Override
	public String suggestFilename()
		{
		return "junit-report.xml";
		}

	@Override
	public void write(@Nonnull OutputStream outstream)
		{
		PrintWriter writer = new PrintWriter(new OutputStreamWriter(outstream, StandardCharsets.UTF_8));   // use the UTF-8 so that chars are encoded correctly for JUnit XML parser

		writer.println("<testsuite>");

		for (TaskResult result : _results)
			{
			String suite_name = _suite_name;
			String test_name = result.getName();
			String failure_type = null;
			String failure_message = null;

			// create error info
			if (!result.isPass())
				{
				if (result.hasErrors())
					{
					failure_type = "error";
					failure_message = result.getSummary();
					}
				else if (result.hasFailures())
					{
					failure_type = "failure";
					failure_message = result.getSummary();
					}
				}

			// write the output
			writer.println(String.format("    <testcase classname=\"%s\" name=\"%s\">", HtmlEscapers.htmlEscaper().escape(suite_name), HtmlEscapers.htmlEscaper().escape(test_name)));
			if (failure_type != null)
				writer.println(String.format("        <%s message=\"%s\"/>", failure_type, HtmlEscapers.htmlEscaper().escape(failure_message)));

			EventLog log = _logs.get(result);
			if (log != null)
				{
				writer.println("        <system-out>");
				ByteArrayOutputStream eventlog_bytes = new ByteArrayOutputStream();
				EventLogPlainTextPrinter printer = new EventLogPlainTextPrinter(new PrintStream(eventlog_bytes));
                try
                    {
                    EventLogPrinter.printAll(log, printer);
                    }
                catch (IOException e)
                    {
                    eventlog_bytes.writeBytes(e.getMessage().getBytes());
                    }
                writer.println(HtmlEscapers.htmlEscaper().escape(new String(eventlog_bytes.toByteArray())));
				writer.println("        </system-out>");
				}

			writer.println("    </testcase>");
			}

		writer.println("</testsuite>");
		writer.flush();
		}

	@Override
	public Object read(@Nonnull InputStream instream) throws IOException
		{
		throw new IOException("JUnitReportData cannot be de-serialized. The format is intended only for consumers of JUnit Report data and is write-only in Muse.");
		}

	public synchronized void addResult(@NotNull TaskResult result, @Nullable EventLog log)
		{
		_results.add(result);
		if (log != null)
			_logs.put(result, log);
		}

	public void setSuiteName(String name)
		{
		_suite_name = name;
		_name = "Junit Report for " + name;
		}

	private List<TaskResult> _results = new ArrayList<>();
	private Map<TaskResult, EventLog> _logs = new HashMap<>();

	private String _suite_name = null;
	private String _name = "JUnit Report";
	}


