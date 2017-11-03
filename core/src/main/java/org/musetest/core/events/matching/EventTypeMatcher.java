package org.musetest.core.events.matching;

import org.musetest.core.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class EventTypeMatcher implements EventMatcher
    {
    public EventTypeMatcher(String type_id)
        {
        _type = type_id;
        }

    @Override
    public boolean matches(MuseEvent event)
        {
        return _type.equals(event.getTypeId());
        }

    private String _type;
    }


