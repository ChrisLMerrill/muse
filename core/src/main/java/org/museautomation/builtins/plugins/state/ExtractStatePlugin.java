package org.museautomation.builtins.plugins.state;

import org.museautomation.core.*;
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
    public void initialize(MuseExecutionContext context)
        {
        context.addEventListener(event ->
            {
            if (event.getTypeId().equals(EndTaskEventType.TYPE_ID))
                extractStates(context);
            });
        }

    private void extractStates(MuseExecutionContext context)
        {
        List<InjectStatePlugin> injectors = Plugins.findAll(InjectStatePlugin.class, context);

        for (StateDefinition state_def : _state_defs)
            {
            InterTaskState state = new InterTaskState();
            state.setStateDefinitionId(state_def.getId());
            for (StateValueDefinition value_def : state_def.getValues())
                {
                Object value = findValue(value_def, injectors, context);
                if (value == null)
                    {
                    if (value_def.isRequired())
                        MessageEventType.raiseWarning(context, String.format("The value for value %s is missing but is required to build the state (%s).", value_def.getName(), state_def.getId()));
                    }
                else if (value_def.getType().isInstance(value))
                    state.setValue(value_def.getName(), value);
                else
                    MessageEventType.raiseWarning(context, String.format("The value for value %s (output state type %s), is not the expected resource type (%s). Instead it is a %s", value_def.getName(), state_def.getDisplayName(), value_def.getType().getName(), value.getClass().getSimpleName()));
                }

            _states.add(state);
            }
        }

    private Object findValue(StateValueDefinition value_def, List<InjectStatePlugin> injectors, MuseExecutionContext context)
        {
        Object value = context.outputs().getOutput(value_def.getName());
        if (value != null)
            return value;
        for (InjectStatePlugin injector : injectors)
            for (InterTaskState state : injector.getStates())
                {
                value = state.getValues().get(value_def.getName());
                if (value != null)
                    return value;
                }
        return null;
        }

    public void addStateToExtract(StateDefinition state_def)
        {
        _state_defs.add(state_def);
        }

    public List<InterTaskState> getExtractedStates()
        {
        return _states;
        }

    public InterTaskState getExtractedState(StateDefinition state_def)
        {
        for (InterTaskState state : _states)
            if (state.getStateDefinitionId().equals(state_def.getId()))
                return state;
        return null;
        }

    private List<InterTaskState> _states = new ArrayList<>();
    private List<StateDefinition> _state_defs = new ArrayList<>();
    }