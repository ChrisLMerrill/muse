package org.musetest.core.events;

import org.musetest.core.*;
import org.musetest.core.plugins.*;
import org.musetest.core.util.*;

import java.io.*;
import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class EventLogPrinter implements MusePlugin, MuseEventListener
    {
    public EventLogPrinter()
	    {
	    _indent_stack.push(" ");
	    }

    @SuppressWarnings("WeakerAccess")  // public API
    public EventLogPrinter(PrintStream out)
        {
        _out = out;
        _indent_stack.push(" ");
        }

    @Override
    public boolean conditionallyAddToContext(MuseExecutionContext context, boolean automatic)
	    {
	    context.addPlugin(this);
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

        _out.print(DurationFormat.formatMinutesSeconds(event.getTimestamp()));

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

    @Override
    public String getId()
	    {
	    return "no/id";
	    }

    @Override
    public void shutdown()
	    {

	    }

    private PrintStream _out;
    private Stack<String> _indent_stack = new Stack<>();
    private EventTypes _types = EventTypes.DEFAULT;
    }
