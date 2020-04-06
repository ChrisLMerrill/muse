package org.museautomation.builtins.plugins.state;

import org.museautomation.core.*;
import org.museautomation.core.context.*;
import org.museautomation.core.events.*;
import org.museautomation.core.plugins.*;
import org.museautomation.core.resource.generic.*;
import org.museautomation.core.task.*;
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
        MuseTask task = TaskExecutionContext.findTask(context);
        for (InterTaskState state : _states)
            {
            StateDefinition state_def = context.getProject().getResourceStorage().getResource(state.getStateDefinitionId(), StateDefinition.class);
            for (StateValueDefinition value_def : state_def.getValues())
                {
                TaskInput input = task.getInputSet().getInput(value_def.getName());
                if (!(input.getType().equals(value_def.getType()) && input.isRequired()))
                    MessageEventType.raiseMessageAndThrowError(context, String.format("The task input type (%s) does not match the input value type (%s). This is likely a configuration problem.", input.getType().getName(), value_def.getType().getName()));
                Object value = state.getValues().get(value_def.getName());
                if (!input.getType().isInstance(value))
                    MessageEventType.raiseMessageAndThrowError(context, String.format("The value in the state named %s does not match the declared type (%s). The value is a %s: %s", input.getName(), input.getType().getName(), value.getClass().getSimpleName(), value));
                context.setVariable(input.getName(), value);
                }
            }
        }

    public void addState(InterTaskState state)
        {
        _states.add(state);
        }

    public List<InterTaskState> getStates()
        {
        return _states;
        }

    private List<InterTaskState> _states = new ArrayList<>();
    }