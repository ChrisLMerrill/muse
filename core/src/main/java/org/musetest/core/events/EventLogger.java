package org.musetest.core.events;

import org.jetbrains.annotations.*;
import org.musetest.core.*;
import org.musetest.core.datacollection.*;
import org.musetest.core.plugins.*;
import org.musetest.core.resource.generic.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class EventLogger extends GenericConfigurablePlugin implements MuseEventListener, DataCollector
    {
    public EventLogger()
	    {
	    super(new EventLoggerConfiguration.EventLoggerType().create());
	    }

    @Override
    protected boolean applyToContextType(MuseExecutionContext context)
	    {
	    return true;
	    }

    EventLogger(GenericResourceConfiguration configuration)
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
	public List<TestResultData> getData()
		{
		return Collections.singletonList(_log);
		}

    public EventLog getLog()
	    {
	    return _log;
	    }

	private EventLog _log = new EventLog();

    public final static String TYPE_ID = "event-logger";
    }