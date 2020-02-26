package org.museautomation.core.events.matching;

import org.museautomation.core.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class EventTagMatcher implements EventMatcher
    {
    public EventTagMatcher(String _tag)
        {
        this._tag = _tag;
        }

    @Override
    public boolean matches(MuseEvent event)
        {
        return event.hasTag(_tag);
        }

    private String _tag;
    }


