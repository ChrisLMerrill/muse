package org.musetest.core.resource;


import org.musetest.core.*;
import org.musetest.core.util.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public interface ResourceStore
    {
    void addResource(MuseResource resource);
    List<MuseResource> findResources(ResourceMetadata matcher);
    ClassLoader getContextClassloader();
    ClassLocator getClassLocator();

    /**
     * Save the resources to persist changes
     *
     * @param resource The resource to save
     * @return null on success, else a string indicating the error to show to the user.
     */
    String saveResource(MuseResource resource);
    }

