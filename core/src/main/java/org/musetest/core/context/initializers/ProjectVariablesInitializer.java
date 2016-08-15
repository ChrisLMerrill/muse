package org.musetest.core.context.initializers;

import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.resource.*;
import org.musetest.core.values.*;
import org.musetest.core.variables.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class ProjectVariablesInitializer implements ContextInitializer
    {
    @Override
    public void initialize(MuseProject project, MuseExecutionContext context) throws MuseExecutionError
        {
        List<VariableList> lists = project.findResources(new ResourceMetadata(new VariableList.VariableListType()), VariableList.class);
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
    }


