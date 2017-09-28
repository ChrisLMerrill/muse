package org.musetest.core.context.initializers;

import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.resource.types.*;
import org.musetest.core.values.*;
import org.musetest.core.variables.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("varlist-initializers")
public class VariableListContextInitializerConfigurations extends ContextInitializerConfiguration
    {
    @SuppressWarnings("unused") // public API for UI
    public List<VariableListContextInitializerConfiguration> getVariableListInitializers()
        {
        return Collections.unmodifiableList(_var_lists);
        }

    @Override
    public ContextInitializer createInitializer()
        {
        return context ->
            {
            final MuseProject project = context.getProject();
            for (VariableListContextInitializerConfiguration initializer : _var_lists)
                {
                // put the list name into the context (for evaluation by value sources if needed)
                context.setVariable(ProjectVarsInitializerSysvarProvider.VARIABLE_LIST_ID_VARNAME, initializer.getListId());

                // evaluate the include condition to determine if the list in this initializer should be included
                ValueSourceConfiguration condition_config = initializer.getIncludeCondition();
                MuseValueSource condition_source = condition_config.createSource(project);
                Object condition = condition_source.resolveValue(context);
                Boolean set_list;
                if (condition instanceof Boolean)
                    set_list = (Boolean) condition;
                else
                    throw new IllegalArgumentException("The condition source of a VariableListContextInitializerConfiguration must resolve to a boolean value. The source (" + condition_source + ") resolved to " + condition);

                if (set_list)
                    {
                    // set the variables in the list into the context
                    String list_id = initializer.getListId().createSource(project).resolveValue(context).toString();
                    VariableList list = project.getResourceStorage().getResource(list_id, VariableList.class);
                    for (String name : list.getVariables().keySet())
                        {
                        ValueSourceConfiguration config = list.getVariables().get(name);
                        Object value = config.createSource(project).resolveValue(context);
                        context.setVariable(name, value, VariableScope.Execution);
                        }
                    }
                }
            context.setVariable(ProjectVarsInitializerSysvarProvider.VARIABLE_LIST_ID_VARNAME, null);
            };
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
    public static class ContextInitializerConfigurationsResourceType extends ResourceSubtype
        {
        public ContextInitializerConfigurationsResourceType()
            {
            super(VariableListContextInitializerConfigurations.class.getAnnotation(MuseTypeId.class).value(), "Variable List Initializer", VariableListContextInitializerConfigurations.class, new ContextInitializerConfiguration.ContextInitializerResourceType());
            }

        @Override
        public boolean isSubtype()
            {
            return true;
            }
        }

    }