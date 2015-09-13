package org.musetest.core;

import org.musetest.core.resource.*;
import org.musetest.core.step.descriptor.*;
import org.musetest.core.step.factory.*;
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

    /*
     * A convenience method for finding a resource that has only a single match.
     */
    MuseResource findResource(ResourceMetadata filter);

    /*
     * A convenience method for finding a resource matching and id and resource type
     */
    <T> T findResource(String id, Class<T> interface_class);

    /**
     * Saves changes to a resource via the ResourceStore.
     *
     * @return null if success, else an error message
     */
    String saveResource(MuseResource resource);

    /**
     * Open and initialize the project
     */
    void open();

    /**
     * Get a classloader that includes classes declared in the project
     */
    ClassLoader getClassloader();

    StepFactory getStepFactory();
    ClassLocator getClassLocator();
    StepDescriptors getStepDescriptors();
    ValueSourceDescriptors getValueSourceDescriptors();
    }
