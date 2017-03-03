package org.musetest.core.context.initializers;

import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.resource.*;
import org.musetest.core.values.*;
import org.musetest.core.variables.*;

import java.util.*;

/**
 * Looks for all the VariableList resources in the project and (selectively) injects those
 * variables into the context.  Selection is achieved by evaluating the VariableListContextInitializerConfigurations
 * found in the ContextInitializerConfigurations in the project. If ContextInitializerConfigurations are found, all
 * VariableLists are injected.
 *
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class VariableListsInitializer implements ContextInitializer
    {
    @Override
    public void initialize(MuseExecutionContext context) throws MuseExecutionError
        {
        MuseProject project = context.getProject();
        List<ResourceToken> tokens = project.getResourceStorage().findResources(new ResourceAttributes(new VariableList.VariableListResourceType()));
        List<VariableList> lists = project.getResourceStorage().getResources(tokens, VariableList.class);
        lists = filterLists(lists, project, context);
        for (VariableList list : lists)
            {
            for (String name : list.getVariables().keySet())
                {
                ValueSourceConfiguration config = list.getVariables().get(name);
                Object value = config.createSource(project).resolveValue(context);
                context.setVariable(name, value, VariableScope.Execution);
                }
            }
        }

    private List<VariableList> filterLists(List<VariableList> lists, MuseProject project, MuseExecutionContext context) throws MuseInstantiationException, ValueSourceResolutionError
        {
        List<ResourceToken> tokens = project.getResourceStorage().findResources(new ResourceAttributes(new ContextInitializerConfigurations.ContextInitializerConfigurationsResourceType()));
        List<ContextInitializerConfigurations> list_of_configs = project.getResourceStorage().getResources(tokens, ContextInitializerConfigurations.class);
        if (list_of_configs.size() == 0)
            return lists; // if there are no configurations, then apply all lists.

        List<String> ids_of_lists_to_keep = new ArrayList<>();
        for (ContextInitializerConfigurations configs : list_of_configs)
            {
            for (VariableListContextInitializerConfiguration config : configs.getVariableListInitializers())
                {
                context.setVariable(ProjectVarsInitializerSysvarProvider.VARIABLE_LIST_ID_VARNAME, config.getVariableListId());
                ValueSourceConfiguration condition_config = config.getIncludeCondition();
                MuseValueSource condition = condition_config.createSource();
                Object result = condition.resolveValue(context);
                Boolean include_it;
                if (result instanceof Boolean)
                    include_it = (Boolean) result;
                else
                    throw new IllegalArgumentException("The condition source of a VariableListContextInitializerConfiguration must resolve to a boolean value. The source (" + condition + ") resolved to " + result);

                if (include_it)
                    ids_of_lists_to_keep.add(config.getVariableListId());
                }
            }

        // map them by id
        Map<String, VariableList> map_of_lists = new HashMap<>();
        for (VariableList list : lists)
            map_of_lists.put(list.getId(), list);

        // create a new list, ordered as they appear in the initializer list
        List<VariableList> filtered_list = new ArrayList<>();
        for (String id_of_list : ids_of_lists_to_keep)
            filtered_list.add(map_of_lists.get(id_of_list));

        return filtered_list;
        }
    }


