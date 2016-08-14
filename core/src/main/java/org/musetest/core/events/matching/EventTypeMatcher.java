package org.musetest.core.events.matching;

import org.musetest.core.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class EventTypeMatcher implements EventMatcher
    {
    public EventTypeMatcher(MuseEventType type)
        {
        _type = type;
        }

    @Override
    public boolean matches(MuseEvent event)
        {
        return _type.equals(event.getType());
        }

    private MuseEventType _type;
    }


