package org.museautomation.builtins.plugins.state;

import org.museautomation.core.*;
import org.museautomation.core.context.*;
import org.museautomation.core.events.*;
import org.museautomation.core.plugins.*;
import org.museautomation.core.resource.generic.*;
import org.museautomation.core.task.state.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class InjectStatePlugin extends GenericConfigurablePlugin
    {
    public InjectStatePlugin(GenericResourceConfiguration configuration)
        {
        super(configuration);
        }

    @Override
    protected boolean applyToContextType(MuseExecutionContext context)
        {
        return false;
        }

    @Override
    public void initialize(MuseExecutionContext context) throws MuseExecutionError
        {
        StateStore provider = Plugins.findType(StateStore.class, context);
        if (provider == null)
            {
            MessageEventType.raiseMessageAndThrowError(context, "No StateProvider plugin was found. InjectStatePlugin is unable to inject state into context.");
            return;
            }
        MuseTask task = TaskExecutionContext.findTask(context);
        for (String state_type : task.getInputStates().getTypeList())
            {
            MuseResource resource = context.getProject().getResourceStorage().findResource(state_type).getResource();
            if (!(resource instanceof StateDefinition))
                {
                MessageEventType.raiseMessageAndThrowError(context, String.format("state type %s, in the task input states, is not the expected resource type (StateDefinition). Instead it is a %s", state_type, resource.getType().getName()));
                return;
                }

            StateDefinition state_def = (StateDefinition) resource;
            List<InterTaskState> states = provider.findStates(state_type);
            if (states.size() == 0)
                MessageEventType.raiseMessageAndThrowError(context, "No states found with type " + state_type);
            if (states.size() > 1)
                MessageEventType.raiseMessageAndThrowError(context, "Multiple states found with type " + state_type);
            InterTaskState state = states.get(0);
            Map<String, Object> values = state.getValues();
            for (String name : values.keySet())
                {
                Object value = values.get(name);
                StateValueDefinition state_value = state_def.getValue(name);
                if (state_value == null)
                    MessageEventType.raiseWarning(context, String.format("Found value in state that was not in the definition. Ignoring %s=%s", name, value));
                else
                    {
                    if (state_value.getType().isInstance(value))
                        context.setVariable(name, value);
                    else
                        MessageEventType.raiseError(context, String.format("Found value in state that was the type declared in the definition. Ignoring %s=%s", name, value));
                    }
                }
            }
        }
    }