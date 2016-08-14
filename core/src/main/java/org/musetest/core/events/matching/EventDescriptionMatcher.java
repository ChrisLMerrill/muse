package org.musetest.core.events.matching;

import org.musetest.core.*;

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
        return event.getDescription().contains(_contains);
        }

    private String _contains;
    }


