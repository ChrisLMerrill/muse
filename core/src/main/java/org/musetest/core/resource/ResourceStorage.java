package org.musetest.core.resource;


import org.musetest.core.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public interface ResourceStorage
    {
    ResourceToken addResource(MuseResource resource);

    boolean removeResource(ResourceToken token);

    List<ResourceToken> findResources(ResourceAttributes attributes);
    <T extends MuseResource> List<T> getResources(List<ResourceToken> tokens, Class<T> implementing_class);
    <T extends MuseResource> T getResource(ResourceToken<T> token);
    MuseResource getResource(String id);
    List<MuseResource> getResources(List<ResourceToken> tokens);

    ClassLoader getContextClassloader();

    ClassLocator getClassLocator();

    /**
     * Save the resources to persist changes
     *
     * @param resource The resource to save
     * @return null on success, else a string indicating the error to show to the user.
     */
    String saveResource(MuseResource resource);

    /**
     * Event listeners
     */
    boolean addResourceListener(ProjectResourceListener listener);

    boolean removeResourceListener(ProjectResourceListener listener);
    }

