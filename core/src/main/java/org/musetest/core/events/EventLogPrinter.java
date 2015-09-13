package org.musetest.core.events;

import org.musetest.core.*;
import org.musetest.core.util.*;

import java.io.*;
import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class EventLogPrinter
    {
    public EventLogPrinter(PrintStream out)
        {
        _out = out;
        _indent_stack.push(" ");
        }

    public EventLogPrinter(PrintStream out, long first_event_nanos)
        {
        _out = out;
        _first_time = first_event_nanos;
        _indent_stack.push(" ");
        }

    public void print(MuseEvent event)
        {
        if (_first_time == -1) // first time
            _first_time = System.nanoTime();

        if (event.getType().equals(MuseEventType.StartStep))
            _indent_stack.push(_indent_stack.peek() + "  ");

        _out.print(DurationFormat.formatMinutesSeconds(event.getTimestampNanos() - _first_time));
        _out.print(_indent_stack.peek());
        _out.println(event.getDescription());

        if (event.getType().equals(MuseEventType.EndStep))
            _indent_stack.pop();
        }

    private PrintStream _out;
    private long _first_time = -1;
    private Stack<String> _indent_stack = new Stack<>();
    }


