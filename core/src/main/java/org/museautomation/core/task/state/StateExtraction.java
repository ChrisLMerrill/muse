package org.museautomation.core.task.state;

import org.museautomation.core.context.*;
import org.museautomation.core.events.*;
import org.museautomation.core.task.input.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class StateExtraction
    {
    public StateExtraction(StateTransitionContext trans_context, TaskExecutionContext task_context, TaskInputResolutionResults task_inputs, InterTaskState input_state)
        {
        _trans_context = trans_context;
        _task_context = task_context;
        _task_inputs = task_inputs;
        _input_state = input_state;
        }

    public boolean execute()
        {
        for (StateTransitionConfiguration.TransitionOutputState outstate : _trans_context.getConfig().getOutputStates())
            {
            StateDefinition state_def = _trans_context.getProject().getResourceStorage().getResource(outstate.getStateId(), StateDefinition.class);
            InterTaskState extracted = extractState(state_def);
            _states.add(extracted);
            }

        return isSuccess();
        }

    public boolean isSuccess()
        {
        return getFailureMessage() == null;
        }

    public String getFailureMessage()
        {
        for (StateTransitionConfiguration.TransitionOutputState outstate : _trans_context.getConfig().getOutputStates())
            {
            StateDefinition state_def = _trans_context.getProject().getResourceStorage().getResource(outstate.getStateId(), StateDefinition.class);
            if (outstate.isRequired())
                {
                InterTaskState state = getState(outstate.getStateId());
                if (state == null)
                    return String.format("State %s is required, was not extracted.", state_def.getId());
                if (!state_def.isValid(state))
                    return String.format("State %s is required, but is not valid or is not present.", state_def.getId());
                }
            }
        return null;
        }

    public InterTaskState getState(String state_id)
        {
        for (InterTaskState state : _states)
            if (state_id.equals(state.getStateDefinitionId()))
                return state;
        return null;
        }

    private InterTaskState extractState(StateDefinition state_def)
        {
        InterTaskState state = new InterTaskState();
        state.setStateDefinitionId(state_def.getId());
        for (StateValueDefinition value_def : state_def.getValues())
            {
            Object value = findValue(value_def);
            if (value == null)
                {
                if (value_def.isRequired())
                    MessageEventType.raiseWarning(_trans_context, String.format("The value for value %s is missing but is required to build the state (%s).", value_def.getName(), state_def.getId()));
                }
            else if (value_def.getType().isInstance(value))
                state.setValue(value_def.getName(), value);
            else
                MessageEventType.raiseWarning(_trans_context, String.format("The value for value %s (output state type %s), is not the expected resource type (%s). Instead it is a %s", value_def.getName(), state_def.getDisplayName(), value_def.getType().getName(), value.getClass().getSimpleName()));
            }
        return state;
        }

    public List<InterTaskState> getValidStates()
        {
        List<InterTaskState> valid = new ArrayList<>();
        for (StateTransitionConfiguration.TransitionOutputState outstate : _trans_context.getConfig().getOutputStates())
            {
            StateDefinition state_def = _trans_context.getProject().getResourceStorage().getResource(outstate.getStateId(), StateDefinition.class);
            InterTaskState state = getState(outstate.getStateId());
            if (!state_def.isValid(state))
                valid.add(state);
            }
        return valid;
        }

    private Object findValue(StateValueDefinition value_def)
        {
        Object value;

        // look in the declared outputs
        value = _task_context.outputs().getOutput(value_def.getName());
        if (value != null)
            return value;

        // look in the task input
        value = _input_state.getValues().get(value_def.getName());
        if (value != null)
            return value;

        // look in the task inputs
        value = _task_inputs.getResolvedInput(value_def.getName());
        if (value != null)
            return value;

        // look in the task-level variables
        value = _task_context.getVariable(value_def.getName());
        return value;
        }

    private final StateTransitionContext _trans_context;
    private final TaskExecutionContext _task_context;
    private final TaskInputResolutionResults _task_inputs;
    private final InterTaskState _input_state;
    private final List<InterTaskState> _states = new ArrayList<>();
    }