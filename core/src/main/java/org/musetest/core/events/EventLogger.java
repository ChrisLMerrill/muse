package org.musetest.core.events;

import org.musetest.core.*;
import org.musetest.core.datacollection.*;
import org.musetest.core.test.plugins.*;

import javax.annotation.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class EventLogger implements MuseEventListener, DataCollector
    {
	@Override
	public void initialize(MuseExecutionContext context)
		{
		context.addEventListener(this);
		}

    @Override
    public String getType()
	    {
	    return TYPE_ID;
	    }

    @Override
    public void configure(@Nonnull TestPluginConfiguration configuration)
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

    public final static String TYPE_ID = "event-logger";

    @SuppressWarnings("unused") // used by reflection
  	public static class EventLoggerType extends TestPluginType
  		{
  		@Override
  		public String getTypeId()
  			{
  			return TYPE_ID;
  			}

  		@Override
  		public String getDisplayName()
  			{
  			return "Event Logger";
  			}

  		@Override
  		public String getShortDescription()
  			{
  			return "Captures a log of test events";
  			}
  		}

    }