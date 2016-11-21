package org.musetest.core.resource;


import org.musetest.core.*;

import java.io.*;
import java.util.*;

/**
 * Provides APIs to the storage of resources in a project.
 *
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public interface ResourceStorage
    {
    ResourceToken addResource(MuseResource resource) throws IOException;

    boolean removeResource(ResourceToken token);

    /**
     * Find resources matching the attributes.
     */
    List<ResourceToken> findResources(ResourceAttributes attributes);

    /**
     * Get the resource with the id.
     */
    MuseResource getResource(String id);

    /*
     * A convenience method for finding a resource matching an id that is the supplied class.
     *
     * @param id The id of the resource
     * @param interface_class The class of the resource.
     * @return A resource matching the provided id and interface_class
     */
    <T extends MuseResource> T getResource(String id, Class<T> implementing_class);

    /**
     * Get a list of resources corresponding to the supplied list of tokens.
     *
     * If a resource cannot be found for any reason (i.e. it was deleted since the token was created), the
     * corresponding entry in the list will be null.
     */
    <T extends MuseResource> List<T> getResources(List<ResourceToken> tokens, Class<T> implementing_class);

    /**
     * Fetch the resource corresonding to the supplied token.
     * @return The resource, or null if it cannot be found (e.g. it was deleted since the token was created).
     */
    <T extends MuseResource> T getResource(ResourceToken<T> token);

    /**
     * Get the token for a specfic resource.
     */
    ResourceToken findResource(String id);

    /**
     * Fetch all the resources for the tokens.
     *
     * If a resource cannot be found for any reason (i.e. it was deleted since the token was created), the
     * corresponding entry in the list will be null.
     */
    List<MuseResource> getResources(List<ResourceToken> tokens);

    ClassLoader getContextClassloader();

    ClassLocator getClassLocator();

    /**
     * Save the resources to persist changes.
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

