package org.musetest.core.events.matching;

import org.musetest.core.*;
import org.musetest.core.events.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class EventDescriptionMatcher implements EventMatcher
    {
    public EventDescriptionMatcher(String contains)
        {
        _contains = contains;
        }

    @Override
    public boolean matches(MuseEvent event)
        {
        final String description = _types.findType(event).getDescription(event);
        return description != null && description.contains(_contains);
        }

    private String _contains;
    private EventTypes _types = EventTypes.DEFAULT;
    }