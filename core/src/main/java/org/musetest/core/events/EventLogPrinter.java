package org.musetest.core.events;

import org.musetest.core.*;
import org.musetest.core.test.plugin.*;
import org.musetest.core.util.*;

import java.io.*;
import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class EventLogPrinter implements TestPlugin, MuseEventListener
    {
    public EventLogPrinter()
	    {
	    _indent_stack.push(" ");
	    }

    @SuppressWarnings("WeakerAccess")  // public API
    public EventLogPrinter(PrintStream out, long first_event_nanos)
        {
        _out = out;
        _first_time = first_event_nanos;
        _indent_stack.push(" ");
        }

    @Override
    public boolean shouldAddToTestContext(MuseExecutionContext context, boolean automatic)
	    {
	    context.addTestPlugin(this);
	    return true;
	    }

    public void setOutput(PrintStream out)
	    {
	    _out = out;
	    }

    @Override
    public void initialize(MuseExecutionContext context)
	    {
	    context.addEventListener(this);
	    }

    @Override
    public void eventRaised(MuseEvent event)
	    {
	    print(event);
	    }

    public void print(MuseEvent event)
        {
        if (_out == null)
        	_out = System.out;

        if (_first_time == -1) // first time
            _first_time = event.getTimestampNanos();

        _out.print(DurationFormat.formatMinutesSeconds(event.getTimestampNanos() - _first_time));

        if (event.getTypeId().equals(EndStepEventType.TYPE_ID) && !event.hasTag(StepEventType.INCOMPLETE) && _indent_stack.size() > 1)  // never pop the first indent (something else has gone wrong).
            _indent_stack.pop();
        if (!_indent_stack.isEmpty()) // this should never happen, but just in case...avoid an exception
            _out.print(_indent_stack.peek());
        _out.println(_types.findType(event).getDescription(event));

        if (event.getTypeId().equals(StartStepEventType.TYPE_ID))
            _indent_stack.push(_indent_stack.peek() + "  ");
        }

    private PrintStream _out;
    private long _first_time = -1;
    private Stack<String> _indent_stack = new Stack<>();
    private EventTypes _types = EventTypes.DEFAULT;
    }
