package org.musetest.core.events;

import org.jetbrains.annotations.*;
import org.musetest.core.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class EventLogger implements MuseEventListener
    {
    @Override
    public void eventRaised(MuseEvent event)
        {
        _log.add(event);
        }

	@NotNull
	public EventLog getLog()
		{
		return _log;
		}

    private EventLog _log = new EventLog();
    }