package org.musetest.core;

import org.musetest.builtins.value.property.*;
import org.musetest.builtins.value.sysvar.*;
import org.musetest.core.resource.*;
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
    void addResource(MuseResource resource);

    List<MuseResource> findResources(ResourceMetadata filter);

    <T extends MuseResource> List<T> findResources(ResourceMetadata filter, Class T);

    /*
     * A convenience method for finding a resource that has only a single match.
     *
     * @param filter Filters to apply to the search
     * @return A resource matching the filters or null if no matches found
     */
    MuseResource findResource(ResourceMetadata filter);

    /*
     * A convenience method for finding a resource matching and id and resource type
     *
     * @param id The id of the resource
     * @param interface_class The class of the resource.
     * @return A resource matching the provided id and interface_class
     */
    <T> T findResource(String id, Class<T> interface_class);

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

    /**
     * Command line options
     */
    void setCommandLineOptions(Map<String, String> options);
    Map<String,String> getCommandLineOptions();
    }
