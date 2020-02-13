package org.museautomation.core;

import org.museautomation.builtins.value.property.*;
import org.museautomation.builtins.value.sysvar.*;
import org.museautomation.core.resource.*;
import org.museautomation.core.resource.types.*;
import org.museautomation.core.step.descriptor.*;
import org.museautomation.core.step.factory.*;
import org.museautomation.core.values.*;
import org.museautomation.core.values.descriptor.*;
import org.museautomation.settings.*;

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
    <T extends ProjectSettingsFile> T getProjectSettings(Class<T> type);

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
