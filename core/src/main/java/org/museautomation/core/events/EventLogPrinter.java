package org.museautomation.core.events;

import org.museautomation.core.*;

import java.io.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public interface EventLogPrinter
    {
    void print(MuseEvent event) throws IOException;
    void finish();

    static void  printAll(EventLog log, EventLogPrinter printer) throws IOException
        {
        for (MuseEvent event : log.getEvents())
            printer.print(event);
        printer.finish();
        }
    }
