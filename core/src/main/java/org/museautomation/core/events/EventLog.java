package org.museautomation.core.events;

import com.fasterxml.jackson.databind.*;
import org.museautomation.core.*;
import org.museautomation.core.datacollection.*;
import org.museautomation.core.events.matching.*;

import javax.annotation.*;
import java.io.*;
import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class EventLog implements TaskResultData
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

	@Override
	public EventLog read(@Nonnull InputStream instream) throws IOException
		{
		ObjectMapper mapper = new ObjectMapper();
		return mapper.reader().readValue(instream);
		}

	public void add(MuseEvent event)
		{
		_events.add(event);
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

	public MuseEvent findLastEvent(EventMatcher matcher)
		{
		for (int i = _events.size() - 1; i >= 0; i--)
            {
            MuseEvent event = _events.get(i);
            if (matcher.matches(event))
                return event;
            }
        return null;
		}

	public List<MuseEvent> getEvents()
		{
		return Collections.unmodifiableList(_events);
		}

	// needed for JSON de/serialization
	public void setEvents(List<MuseEvent> events)
		{
		_events = events;
		}

	@Override
	public String toString()
		{
		return String.format("Event log (%d events)", _events.size());
		}

	private List<MuseEvent> _events = new ArrayList<>();
	}