package org.musetest.core.events;

import org.musetest.core.*;
import org.musetest.core.context.initializers.*;
import org.musetest.core.datacollection.*;
import org.musetest.core.events.matching.*;

import java.io.*;
import java.text.*;
import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class EventLogger implements MuseEventListener, DataCollector
    {
	@Override
	public void initialize(MuseExecutionContext context) throws MuseExecutionError
		{
		context.addEventListener(this);
		}

    @Override
    public String getType()
	    {
	    return "event-logger";
	    }

    @Override
    public void configure(ContextInitializerConfiguration configuration)
	    {
	    // doesn't currently need configuration
	    }

    @Override
    public void eventRaised(MuseEvent event)
        {
        _log.add(event);
        }

	@Override
	public EventLog getData()
		{
		return _log;
		}

	private EventLog _log = new EventLog();
    }


