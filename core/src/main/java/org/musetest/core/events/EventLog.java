package org.musetest.core.events;

import com.fasterxml.jackson.databind.*;
import org.musetest.core.*;
import org.musetest.core.datacollection.*;
import org.musetest.core.events.matching.*;

import javax.annotation.*;
import java.io.*;
import java.text.*;
import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class EventLog implements TestResultData
    {
	public Iterator<MuseEvent> events()
        {
        return _events.iterator();
        }
	private String _name = "EventLog";

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
	    return getName() + ".json";
	    }

    @Override
	public void write(@Nonnull OutputStream outstream) throws IOException
		{
		ObjectMapper mapper = new ObjectMapper();
		mapper.writerWithDefaultPrettyPrinter().writeValue(outstream, this);
		}

	public void add(MuseEvent event)
		{
		_events.add(event);
		}

	private void print(PrintStream out)
        {
        if (_events.size() == 0)
            {
            out.println("Event log: (empty)");
            return;
            }

        out.println("Event log (started " + DateFormat.getDateTimeInstance().format(new Date(_events.get(0).getTimestampNanos())) + "):");
        EventLogPrinter printer = new EventLogPrinter(out, _events.get(0).getTimestampNanos());
        for (MuseEvent event : _events)
            printer.print(event);
        }

	public List<MuseEvent> findEvents(EventMatcher matcher)
      {
      List<MuseEvent> found = new ArrayList<>();
      for (MuseEvent event : _events)
          {
          if (matcher.matches(event))
              found.add(event);
          }
      return found;
      }

  public MuseEvent findFirstEvent(EventMatcher matcher)
      {
      for (MuseEvent event : _events)
          if (matcher.matches(event))
              return event;
      return null;
      }

	public List<MuseEvent> getEvents()
     {
     return Collections.unmodifiableList(_events);
     }

	@Override
    public String toString()
        {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        PrintStream printer = new PrintStream(bytes);
        print(printer);
        printer.flush();
        return bytes.toString();
        }

	private List<MuseEvent> _events = new ArrayList<>();
    }


