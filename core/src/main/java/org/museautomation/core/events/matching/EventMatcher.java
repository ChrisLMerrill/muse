package org.museautomation.core.events.matching;

import org.museautomation.core.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public interface EventMatcher
    {
    boolean matches(MuseEvent event);
    }

