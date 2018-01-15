package org.musetest.core.events;

import org.jetbrains.annotations.*;
import org.musetest.core.*;
import org.musetest.core.datacollection.*;
import org.musetest.core.resource.generic.*;
import org.musetest.core.test.plugin.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class EventLogger extends BaseTestPlugin implements MuseEventListener, DataCollector
    {
    public EventLogger()
	    {
	    super(new EventLoggerConfiguration.EventLoggerType().create());
	    }

    public EventLogger(GenericResourceConfiguration configuration)
	    {
	    super(configuration);
	    }

    @Override
	public void initialize(MuseExecutionContext context)
		{
		context.addEventListener(this);
		}

    @Override
    public void eventRaised(MuseEvent event)
        {
        _log.add(event);
        }

	@Override
	@NotNull
	public EventLog getData()
		{
		return _log;
		}

	private EventLog _log = new EventLog();

    public final static String TYPE_ID = "event-logger";
    }