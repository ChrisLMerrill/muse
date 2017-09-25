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
        List<ResourceToken> tokens = project.getResourceStorage().findResources(new ResourceAttributes(new VariableListContextInitializerConfigurations.ContextInitializerConfigurationsResourceType()));
        List<VariableListContextInitializerConfigurations> list_of_configs = project.getResourceStorage().getResources(tokens, VariableListContextInitializerConfigurations.class);
        if (list_of_configs.size() == 0)
            return lists; // if there are no configurations, then apply all lists.

        List<String> ids_of_lists_to_keep = new ArrayList<>();
        for (VariableListContextInitializerConfigurations configs : list_of_configs)
            {
            for (VariableListContextInitializerConfiguration config : configs.getVariableListInitializers())
                {
                context.setVariable(ProjectVarsInitializerSysvarProvider.VARIABLE_LIST_ID_VARNAME, config.getListId());
                ValueSourceConfiguration condition_config = config.getIncludeCondition();
                MuseValueSource condition_source = condition_config.createSource(context.getProject());
                Object condition = condition_source.resolveValue(context);
                Boolean include_it;
                if (condition instanceof Boolean)
                    include_it = (Boolean) condition;
                else
                    throw new IllegalArgumentException("The condition source of a VariableListContextInitializerConfiguration must resolve to a boolean value. The source (" + condition_source + ") resolved to " + condition);

                if (include_it)
                    {
                    ValueSourceConfiguration id_config = config.getListId();
                    MuseValueSource id_source = id_config.createSource(context.getProject());
                    Object id = id_source.resolveValue(context);
                    if (id != null)
                        ids_of_lists_to_keep.add(id.toString());
                    else
                        throw new IllegalArgumentException("The id source of a VariableListContextInitializerConfiguration must resolve to a non-nullvalue.");
                    }
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


