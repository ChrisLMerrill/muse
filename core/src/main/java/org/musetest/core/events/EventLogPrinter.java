package org.musetest.core.events;

import org.musetest.core.*;
import org.musetest.core.step.*;
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
        if (!_indent_stack.isEmpty()) // this should never happen, but just in case...avoid an exception
            _out.print(_indent_stack.peek());
        _out.println(event.getDescription());

        if (event.getType().equals(MuseEventType.EndStep) && !((StepEvent)event).getResult().getStatus().equals(StepExecutionStatus.INCOMPLETE) && _indent_stack.size() > 1)  // never pop the first indent (something else has gone wrong).
            _indent_stack.pop();
        }

    private PrintStream _out;
    private long _first_time = -1;
    private Stack<String> _indent_stack = new Stack<>();
    }


