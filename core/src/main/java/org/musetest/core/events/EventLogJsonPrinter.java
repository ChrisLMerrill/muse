package org.musetest.core.events;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.musetest.core.*;
import org.musetest.core.util.*;

import java.io.*;
import java.text.*;
import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class EventLogJsonPrinter implements EventLogPrinter
    {
    public EventLogJsonPrinter(PrintStream out)
        {
        _out = out;
        _out.println('[');

        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(JsonGenerator.Feature.AUTO_CLOSE_TARGET);
        _writer = mapper.writerWithDefaultPrettyPrinter();
        }

    public void print(MuseEvent event) throws IOException
        {
        if (_events_written > 0)
            _out.println(',');

        _writer.writeValue(_out, event);

        _events_written++;
        }

    @Override
    public void finish()
        {
        _out.println("\n]");
        _out.close();
        _out = null;
        }

    private PrintStream _out;
    private long _events_written = 0;
    private ObjectWriter _writer = null;
    }
