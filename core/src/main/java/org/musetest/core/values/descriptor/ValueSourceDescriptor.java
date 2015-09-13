package org.musetest.core.values.descriptor;

import org.musetest.core.values.*;

/**
 * Provides information about a MuseValueSource that may be useful in the UI.
 *
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public interface ValueSourceDescriptor
    {
    String getType();
    String getName();
    String getShortDescription();
    String getShortDescription(ValueSourceConfiguration source);
    String getGroupName();
    }


