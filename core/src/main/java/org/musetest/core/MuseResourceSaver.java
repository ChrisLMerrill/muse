package org.musetest.core;

/**
 * Saves a resource in a project (typically one loaded from a MuseResourceFactory).
 *
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public interface MuseResourceSaver
    {
    Boolean saveResource(MuseResource resource);
    }

