package org.museautomation.core;

import org.museautomation.core.util.*;

/**
 * Saves a resource in a project (typically one loaded from a MuseResourceFactory).
 *
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public interface MuseResourceSaver
    {
    Boolean saveResource(MuseResource resource, TypeLocator type_locator);
    String getDefaultFileExtension();
    }

