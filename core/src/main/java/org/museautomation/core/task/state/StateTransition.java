package org.museautomation.core.task.state;

import org.museautomation.core.*;
import org.museautomation.core.context.*;
import org.museautomation.core.events.*;
import org.museautomation.core.execution.*;
import org.museautomation.core.plugins.*;
import org.museautomation.core.resource.*;
import org.museautomation.core.task.*;
import org.museautomation.core.task.input.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class StateTransition
    {
    public StateTransition(StateTransitionContext context)
        {
        _context = context;
        }

    public StateTransitionResult execute()
        {
        _result = executeImplementation();
        return _result;
        }

    public StateTransitionResult executeImplementation()
        {
        _context.raiseEvent(StartStateTransitionEventType.create());
        StateTransitionConfiguration config = _context.getConfig();
        ResourceToken<MuseResource> token = _context.getProject().getResourceStorage().findResource(config.getTaskId());
        if (token == null)
            return new StateTransitionResult(String.format("Cannot execute transition. The specified task id (%s) was not found.", config.getTaskId()));
        MuseResource resource = token.getResource();
        if (!new MuseTask.TaskResourceType().equals(resource.getType()))
            return new StateTransitionResult(String.format("Cannot execute transition. The specified task id (%s) is not a task. It is a %s.", config.getTaskId(), resource.getType()));

        MuseTask task = (MuseTask) resource;
        _run_config = new BasicTaskConfiguration(task);
        for (MusePlugin plugin : _plugins)
            _run_config.addPlugin(plugin);
        _run_config.withinContext(_context);  // must initialize this before we can raise any events in the context.

        _context.raiseEvent(StartResolvingTransitionInputsEventType.create());
        // get input state, if present
        InterTaskState input_state = InterTaskState.START;
        if (_context.getConfig().getInputState() != null)
            input_state = _context.getContainer().findState(_context.getConfig().getInputState().getStateId());
        if (input_state == null)
            {
            String message = "Required input state not found: " + _context.getConfig().getInputState().getStateId();
            _context.raiseEvent(EndResolvingTransitionInputsEventType.createFailure(message));
            _context.raiseEvent(EndStateTransitionEventType.create());
            return new StateTransitionResult(message);
            }

        // resolve inputs
        TaskInputResolution input_resolution = new TaskInputResolution(_context, _run_config.context(), input_state);
        TaskInputResolutionResults resolution_result = input_resolution.execute();
        MuseEvent resolved_event;
        if (!resolution_result.inputsSatisfied(task))
            {
            resolved_event = EndResolvingTransitionInputsEventType.createFailure(resolution_result.getUnsatisfiedInputDescription(task));
            _context.raiseEvent(resolved_event);
            _context.raiseEvent(EndStateTransitionEventType.create());
            return new StateTransitionResult(new EndResolvingTransitionInputsEventType().getDescription(resolved_event));
            }

        // inject the inputs
        for (ResolvedTaskInput resolved_input : resolution_result.getResolvedInputs())
            _run_config.context().setVariable(resolved_input.getName(), resolved_input.getValue());
        _context.raiseEvent(EndResolvingTransitionInputsEventType.create());

        // prepare the runner and execute task
        BlockingThreadedTaskRunner runner = new BlockingThreadedTaskRunner(_context, _run_config);
        configureMessageRelay(runner);
        runner.runTask();
        TaskResult task_result = TaskResult.find(_run_config.context());
        if (task_result == null)
            {
            _context.raiseEvent(EndStateTransitionEventType.create());
            return new StateTransitionResult("Unable to find the TaskResult in the TaskExecutionContext. Is is likely a configuration error (such as not having the TaskResultCollector plugin configured).");
            }
        if (!task_result.isPass())
            {
            _context.raiseEvent(EndStateTransitionEventType.create());
            return new StateTransitionResult(task_result);
            }

        StateExtraction extractor = new StateExtraction(_context, _run_config.context(), resolution_result, input_state);
        if (!extractor.execute())
            {
            _context.raiseEvent(EndStateTransitionEventType.create());
            return new StateTransitionResult(extractor.getFailureMessage());
            }
        InterTaskState primary_state = null;
        for (StateTransitionConfiguration.TransitionOutputState outstate : config.getOutputStates())
            {
            InterTaskState state = extractor.getState(outstate.getStateId());
            StateDefinition state_def = _context.getProject().getResourceStorage().getResource(outstate.getStateId(), StateDefinition.class);
            if (state_def.isValid(state))
                {
                if (outstate.isReplacesInput() && _context.getContainer().contains(input_state))
                    _context.getContainer().replaceState(input_state, state);
                else
                    _context.getContainer().addState(state);
                if (outstate.isRequired())
                    primary_state = state;
                }
            else
                {
                if (outstate.isRequired())
                    {
                    _context.raiseEvent(EndStateTransitionEventType.create());
                    return new StateTransitionResult(String.format("The required output state %s could not be extracted.", outstate.getStateId()));
                    }
                }
            }
        if (config.getInputState() != null && config.getInputState().isTerminate() && _context.getContainer().contains(input_state))
            _context.getContainer().removeState(input_state);

        if (primary_state == null && extractor.getValidStates().size() > 0)
            primary_state = extractor.getValidStates().get(0);
        StateTransitionResult result = new StateTransitionResult(task_result, primary_state);
        _context.setResult(result);
        _context.raiseEvent(EndStateTransitionEventType.create());
        return result;
        }

    private void configureMessageRelay(BlockingThreadedTaskRunner runner)
        {
        MuseExecutionContext _task_context = runner.getExecutionContext();
        _task_context.addEventListener(event ->
            {
            switch (event.getTypeId())
                {
                case MessageEventType.TYPE_ID:
                case StartTaskEventType.TYPE_ID:
                case EndTaskEventType.TYPE_ID:
                    _context.raiseEvent(event);
                    return;
                }
            if (event.hasTag("status"))
                _context.raiseEvent(event);
            });
        }

    public void addPlugin(MusePlugin plugin)
        {
        _plugins.add(plugin);
        }

    public StateTransitionResult getResult()
        {
        return _result;
        }

    public StateTransitionContext getContext()
        {
        return _context;
        }

    public TaskExecutionContext getTaskContext()
        {
        return _run_config.context();
        }


    private final StateTransitionContext _context;
    private StateTransitionResult _result;
    private final List<MusePlugin> _plugins = new ArrayList<>();
    private TaskConfiguration _run_config;
    }