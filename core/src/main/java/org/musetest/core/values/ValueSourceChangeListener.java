package org.musetest.core.values;

import org.musetest.core.values.events.*;

/**
 * Listen for change events on ValueSourceConfigurations.
 *
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public interface ValueSourceChangeListener
    {
    void changed(ValueSourceChangeEvent event);
    }

