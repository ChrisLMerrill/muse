package org.musetest.core.events.matching;

import org.musetest.core.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public interface EventMatcher
    {
    boolean matches(MuseEvent event);
    }

