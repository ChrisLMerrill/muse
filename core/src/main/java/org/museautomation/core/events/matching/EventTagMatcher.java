package org.museautomation.core.events.matching;

import org.museautomation.core.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class EventTagMatcher implements EventMatcher
    {
    public EventTagMatcher(String... tags)
        {
        _tags.addAll(Arrays.asList(tags));
        }

    @Override
    public boolean matches(MuseEvent event)
        {
        for (String tag : _tags)
            if (event.hasTag(tag))
                return true;
        return false;
        }

    private Set<String> _tags = new HashSet<>();
    }


