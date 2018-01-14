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
    /**
     * Open and initialize the project
     */
    void open();

    String getName();

    /**
     * Provides access to project resources.
     */
    ResourceStorage getResourceStorage();

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
