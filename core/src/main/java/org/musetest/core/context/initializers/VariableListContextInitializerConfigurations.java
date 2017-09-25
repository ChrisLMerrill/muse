package org.musetest.core.context.initializers;

import org.musetest.core.*;
import org.musetest.core.resource.*;
import org.musetest.core.resource.types.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("varlist-initializers")
public class VariableListContextInitializerConfigurations extends BaseMuseResource
    {
    public List<VariableListContextInitializerConfiguration> getVariableListInitializers()
        {
        return Collections.unmodifiableList(_var_lists);
        }

    @SuppressWarnings("unused")  // required for Json de/serialization
    public void setVariableListInitializers(List<VariableListContextInitializerConfiguration> var_lists)
        {
        _var_lists = var_lists;
        }

    public void addVariableListInitializer(VariableListContextInitializerConfiguration config)
        {
        _var_lists.add(config);
        if (_listeners != null)
            for (VariableListContextInitializerChangeListener listener : _listeners)
                listener.variableListInitializerAdded(config);
        }

    public void removeVariableListInitializer(VariableListContextInitializerConfiguration config)
        {
        _var_lists.remove(config);
        if (_listeners != null)
            for (VariableListContextInitializerChangeListener listener : _listeners)
                listener.variableListInitializerRemoved(config);
        }

    public void addContextInitializerChangeListener(VariableListContextInitializerChangeListener listener)
        {
        if (_listeners == null)
            _listeners = new ArrayList<>();
        _listeners.add(listener);
        }

    public void removeContextInitializerChangeListener(VariableListContextInitializerChangeListener listener)
        {
        if (_listeners == null)
            return;

        _listeners.remove(listener);
        if (_listeners.isEmpty())
            _listeners = null;
        }

    @Override
    public ResourceType getType()
        {
        return new ContextInitializerConfigurationsResourceType();
        }

    private List<VariableListContextInitializerConfiguration> _var_lists = new ArrayList<>();
    private transient List<VariableListContextInitializerChangeListener> _listeners;


    @SuppressWarnings("WeakerAccess")  // discovered and instantiated by reflection (see class ResourceTypes)
    public static class ContextInitializerConfigurationsResourceType extends ResourceType
        {
        public ContextInitializerConfigurationsResourceType()
            {
            super(VariableListContextInitializerConfigurations.class.getAnnotation(MuseTypeId.class).value(), "Context Initializer", VariableListContextInitializerConfigurations.class);
            }
        }

    }


