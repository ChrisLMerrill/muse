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
public class ExtractStatePlugin extends GenericConfigurablePlugin
    {
    public ExtractStatePlugin(GenericResourceConfiguration configuration)
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
        _store = Plugins.findType(StateStore.class, context);
        if (_store == null)
            {
            MessageEventType.raiseMessageAndThrowError(context, "No StateProvider plugin was found. ExtractStatePlugin is unable to extract context into a state.");
            return;
            }

        context.addEventListener(event ->
            {
            if (event.getTypeId().equals(EndTaskEventType.TYPE_ID))
                extractStates(context);
            });

        MuseTask task = TaskExecutionContext.findTask(context);
        TaskOutputStates output_states = task.getOutputStates();
        for (String state_type : output_states.getTypeList())
            {
            MuseResource resource = context.getProject().getResourceStorage().findResource(state_type).getResource();
            if (!(resource instanceof StateDefinition))
                {
                MessageEventType.raiseMessageAndThrowError(context, String.format("state type %s, in the task input states, is not the expected resource type (StateDefinition). Instead it is a %s", state_type, resource.getType().getName()));
                return;
                }
            _state_defs.add((StateDefinition)resource);
            }
        }

    private void extractStates(MuseExecutionContext context)
        {
        for (StateDefinition state_def : _state_defs)
            {
            InterTaskState state = new InterTaskState();
            state.setStateDefinitionId(state_def.getId());
            for (StateValueDefinition value_def : state_def.getValues())
                {
                Object value = context.getVariable(value_def.getName());
                if (value == null)
                    {
                    if (value_def.isRequired())
                        MessageEventType.raiseError(context, String.format("The value for value %s is missing but is required to build the state (%s).", value_def.getName(), state_def.getId()));
                    }
                else if (value_def.getType().isInstance(value))
                    state.setValue(value_def.getName(), value);
                else
                    MessageEventType.raiseError(context, String.format("The value for value %s (output state type %s), is not the expected resource type (%s). Instead it is a %s", value_def.getName(), state_def.getDisplayName(), value_def.getType().getName(), value.getClass().getSimpleName()));
                }

            _store.addState(state);
            }
        }

    private StateStore _store;
    private List<StateDefinition> _state_defs = new ArrayList<>();
    }