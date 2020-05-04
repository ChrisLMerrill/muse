package org.museautomation.core.task.input;

import org.museautomation.core.*;
import org.museautomation.core.context.*;
import org.museautomation.core.events.*;
import org.museautomation.core.task.*;
import org.museautomation.core.task.state.*;
import org.museautomation.core.values.strings.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class TaskInputResolution
    {
    public TaskInputResolution(StateTransitionContext transition_context, TaskExecutionContext task_context, InterTaskState input_state)
        {
        if (transition_context == null || task_context == null || input_state == null)
            throw new IllegalArgumentException("All parameters are required non-null");
        _transition_context = transition_context;
        _task_context = task_context;
        _input_state = input_state;
        }

    public TaskInputResolutionResults execute()
        {
        TaskInputResolutionResults results = new TaskInputResolutionResults();

        resolveFromInputState(results);

        // first, try resolving from input providers
        resolveFromInputProviders(results);

        // then, try resolving from defaults
        resolveFromDefaults(results);

        // try resolving from single providers again
        resolveFromInputProviders(results);

        return results;
        }

    private void resolveFromInputState(TaskInputResolutionResults results)
        {
        MuseTask task = _task_context.getTask();
        for (TaskInput input : results.getUnresolvedInputs(task.getInputSet()).all())
            {
            // if not already resolved and contained in state
            Object value = _input_state.getValues().get(input.getName());
            if (value != null)
                results.addResolvedInput(new ResolvedTaskInput(input.getName(), value, new ResolvedInputSource.TaskStateSource(_input_state)));
            }
        }

    private void resolveFromInputProviders(TaskInputResolutionResults results)
        {
        boolean any_resolved = true;
        while (any_resolved)  // do this repeatedly until none are resolved. This allows resolving one input that is dependent on another.
            {
            any_resolved = false;
            for (TaskInputProvider provider : _transition_context.getInputProviders())
                {
                List<ResolvedTaskInput> resolved_list = provider.resolveInputs(results, results.getUnresolvedInputs(_task_context.getTask().getInputSet()), _task_context);
                for (ResolvedTaskInput resolved : resolved_list)
                    {
                    TaskInput input = _task_context.getTask().getInputSet().getInput(resolved.getName());
                    // verify each resolved input is a valid type.
                    if (input.getType().isInstance(resolved.getValue()))
                        {
                        any_resolved = true;
                        results.addResolvedInput(resolved);
                        }
                    else
                        MessageEventType.raiseWarning(_transition_context, String.format("InputProvider (%s) provided a value with the wrong type. Expected a %s, but received a %s (%s)", provider.getDescription(), input.getType().getName(), resolved.getValue().getClass().getSimpleName(), resolved.getValue().toString()));
                    }
                }
            }
        }

    private void resolveFromDefaults(TaskInputResolutionResults results)
        {
        for (TaskInput input : _task_context.getTask().getInputSet().getInputs())
            {
            try
                {
                if (results.getResolvedInput(input.getName()) == null && input.getDefault() != null)
                    {
                    Object value = input.getDefault().createSource(_task_context.getProject()).resolveValue(_transition_context);
                    if (value == null)
                        {
                        if (input.isRequired())
                            MessageEventType.raiseWarning(_transition_context, String.format("Input %s resolved to null, but is required to be a %s.", input.getName(), input.getType().getName()));
                        }
                    else
                        {
                        if (input.getType().isInstance(value))
                            results.addResolvedInput(new ResolvedTaskInput(input.getName(), value, new ResolvedInputSource.DefaultValueSource()));
                        else
                            MessageEventType.raiseWarning(_transition_context, String.format("Input %s resolved to a %s, but is required to be a %s.", input.getName(), value.getClass().getSimpleName(), input.getType().getName()));
                        }
                    }
                }
            catch (MuseExecutionError e)
                {
                MessageEventType.raiseWarning(_transition_context, String.format("Error while resolving input %s using this default value source: %s", input.getName(), _transition_context.getProject().getValueSourceDescriptors().get(input.getDefault()).getInstanceDescription(input.getDefault(), new RootStringExpressionContext(_transition_context.getProject()))));
                }
            }
        }

    private final StateTransitionContext _transition_context;
    private final TaskExecutionContext _task_context;
    private final InterTaskState _input_state;
    }