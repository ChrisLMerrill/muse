package org.museautomation.core.events;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.museautomation.core.*;
import org.museautomation.core.resource.json.*;

import java.io.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class EventLogJsonPrinter implements EventLogPrinter
    {
    public EventLogJsonPrinter(PrintStream out)
        {
        _out = out;
        _out.println('[');

        ObjectMapper mapper = JsonMapperFactory.createDefaultMapper();
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
    private final ObjectWriter _writer;
    }
