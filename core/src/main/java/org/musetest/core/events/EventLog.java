package org.musetest.core.events;

import org.musetest.core.*;

import java.io.*;
import java.text.*;
import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class EventLog implements MuseEventListener
    {
    public Iterator<MuseEvent> events()
        {
        return _events.iterator();
        }

    @Override
    public void eventRaised(MuseEvent event)
        {
        if (_events.size() == 0)
            _start_time = System.currentTimeMillis();
        _events.add(event);
        }

    public void print(PrintStream out)
        {
        if (_events.size() == 0)
            {
            out.println("Event log: (empty)");
            return;
            }

        out.println("Event log (started " + DateFormat.getDateTimeInstance().format(new Date(_start_time)) + "):");
        EventLogPrinter printer = new EventLogPrinter(out, _events.get(0).getTimestampNanos());
        for (MuseEvent event : _events)
            printer.print(event);
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

    public List<MuseEvent> findEvents(MuseEventType type)
        {
        List<MuseEvent> found = new ArrayList<>();
        for (MuseEvent event : _events)
            {
            if (event.getType().equals(type))
                found.add(event);
            }
        return found;
        }

    public boolean hasEventWithDescriptionContaining(String text)
        {
        for (MuseEvent event : _events)
            {
            if (event.getDescription().contains(text))
                return true;
            }
        return false;
        }

    private List<MuseEvent> _events = new ArrayList<>();
    private long _start_time;
    }


