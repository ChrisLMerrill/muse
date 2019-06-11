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
public interface EventLogPrinter
    {
    void print(MuseEvent event) throws IOException;
    void finish();
    }
