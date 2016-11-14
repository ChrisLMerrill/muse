package org.musetest.core;

import org.musetest.builtins.value.property.*;
import org.musetest.builtins.value.sysvar.*;
import org.musetest.core.resource.*;
import org.musetest.core.resource.types.*;
import org.musetest.core.step.descriptor.*;
import org.musetest.core.step.factory.*;
import org.musetest.core.values.*;
import org.musetest.core.values.descriptor.*;

import java.util.*;

/**
 * A MuseProject is a collection of MuseResources (test artifacts).
 *
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public interface MuseProject
    {
    ResourceToken addResource(MuseResource resource);
    boolean removeResource(ResourceToken token);

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
     * Find resources matching the attributes.
     */
    List<ResourceToken> findResources(ResourceAttributes attributes);

    /**
     * Find resources matching the attributes.
     */
    ResourceToken findResource(String id);

    /**
     * Fetch the resource corresonding to the supplied token.
     * @return The resource, or null if it cannot be found (e.g. it was deleted since the token was created).
     */
    <T extends MuseResource> T getResource(ResourceToken<T> token);

    /**
     * Get a list of resources corresponding to the supplied list of tokens.
     *
     * If a resource cannot be found for any reason (i.e. it was deleted since the token was created), the
     * corresponding entry in the list will be null.
     */
    <T extends MuseResource> List<T> getResources(List<ResourceToken> tokens, Class<T> implementing_class);

    /**
     * Fetch all the resources for the tokens.
     *
     * If a resource cannot be found for any reason (i.e. it was deleted since the token was created), the
     * corresponding entry in the list will be null.
     */
    List<MuseResource> getResources(List<ResourceToken> tokens);

    /**
     * Saves changes to a resource via the ResourceStore.
     *
     * @param resource The resource to save
     * @return null if success, else an error message
     */
    String saveResource(MuseResource resource);

    /**
     * Open and initialize the project
     */
    void open();

    String getName();

    /**
     * Get a classloader that includes classes declared in the project
     *
     * @return a classloader
     */
    ClassLoader getClassloader();

    StepFactory getStepFactory();
    ClassLocator getClassLocator();
    StepDescriptors getStepDescriptors();
    ValueSourceDescriptors getValueSourceDescriptors();
    ValueSourceStringExpressionSupporters getValueSourceStringExpressionSupporters();
    PropertyResolvers getPropertyResolvers();
    SystemVariableProviders getSystemVariableProviders();
    @SuppressWarnings("unused") // used by project navigator in UI
    ResourceTypes getResourceTypes();

    /**
     * Command line options
     */
    void setCommandLineOptions(Map<String, String> options);
    Map<String,String> getCommandLineOptions();

    /**
     * Event listeners
     */
    boolean addResourceListener(ProjectResourceListener listener);
    boolean removeResourceListener(ProjectResourceListener listener);
    }
