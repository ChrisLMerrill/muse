package org.musetest.core.events;

import org.musetest.core.*;
import org.musetest.core.plugins.*;
import org.musetest.core.util.*;

import java.io.*;
import java.text.*;
import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class EventLogPlainTextPrinter implements EventLogPrinter
    {
    public EventLogPlainTextPrinter(PrintStream out)
	    {
	    _out = out;
	    _indent_stack.push(" ");
	    }

    public void print(MuseEvent event)
        {
        if (_first_event_timestamp == 0)
            {
	        _out.println("First event raised at " + DateFormat.getDateTimeInstance().format(new Date(event.getTimestamp())));
	        _first_event_timestamp = event.getTimestamp();
	        }

        _out.print(formatTime(event.getTimestamp()));

        if (event.getTypeId().equals(EndStepEventType.TYPE_ID) && !event.hasTag(StepEventType.INCOMPLETE) && _indent_stack.size() > 1)  // never pop the first indent (something else has gone wrong).
            _indent_stack.pop();
        if (!_indent_stack.isEmpty()) // this should never happen, but just in case...avoid an exception
            _out.print(_indent_stack.peek());
        final EventType type = _types.findType(event);
        String description = type.getDescription(event);
        if (description == null)
	        description = type.getName();
        _out.println(description);

        if (event.getTypeId().equals(StartStepEventType.TYPE_ID))
            _indent_stack.push(_indent_stack.peek() + "  ");
        }

    private String formatTime(long timestamp)
        {
        return DurationFormat.formatMinutesSeconds(timestamp - _first_event_timestamp);
        }

    @Override
    public void finish()
        {
        _out.print(formatTime(System.currentTimeMillis()));
        if (!_indent_stack.isEmpty()) // this should never happen, but just in case...avoid an exception
            _out.print(_indent_stack.peek());
        _out.println("Log finished");
        _out.close();
        _out = null;
        }

    private PrintStream _out;
    private Stack<String> _indent_stack = new Stack<>();
    private EventTypes _types = EventTypes.DEFAULT;
    private long _first_event_timestamp = 0;
    }
