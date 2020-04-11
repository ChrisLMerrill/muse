package org.museautomation.core.task.state;

import org.museautomation.builtins.plugins.input.*;
import org.museautomation.builtins.plugins.results.*;
import org.museautomation.builtins.plugins.state.*;
import org.museautomation.core.*;
import org.museautomation.core.events.*;
import org.museautomation.core.execution.*;
import org.museautomation.core.plugins.*;
import org.museautomation.core.resource.*;
import org.museautomation.core.task.*;

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
        _context.raiseEvent(StartStateTransitionEventType.create());
        StateTransitionConfiguration config = _context.getConfig();
        ResourceToken<MuseResource> token = _context.getProject().getResourceStorage().findResource(config.getTaskId());
        if (token == null)
            return new StateTransitionResult(String.format("Cannot execute transition. The specified task id (%s) was not found.", config.getTaskId()));
        MuseResource resource = token.getResource();
        if (!new MuseTask.TaskResourceType().equals(resource.getType()))
            return new StateTransitionResult(String.format("Cannot execute transition. The specified task id (%s) is not a task. It is a %s.", config.getTaskId(), resource.getType()));

        MuseTask task = (MuseTask) resource;
        TaskConfiguration run_config = new BasicTaskConfiguration(task);

_context.raiseEvent(StartResolvingTransitionInputsEventType.create());
// TODO resolve all inputs (from input state and providers in the context/configuration)
        // setup state injector
        InjectStatePlugin inject_plugin = new InjectStatePlugin(new InjectStatePluginConfiguration());
        StateDefinition input_state_def = _context.getProject().getResourceStorage().getResource(config.getInputState().getStateId(), StateDefinition.class);
        InterTaskState input_state = _context.getContainer().findState(input_state_def.getId());
        inject_plugin.addState(input_state);
        run_config.addPlugin(inject_plugin);

        // setup state extractor
        ExtractStatePlugin extract_plugin = null;
        if (config.getOutputStates().size() > 0)
            {
            extract_plugin = new ExtractStatePlugin(new ExtractStatePluginConfiguration());
            for (StateTransitionConfiguration.TransitionOutputState outstate_config : config.getOutputStates())
                {
                StateDefinition output_state_def = _context.getProject().getResourceStorage().getResource(outstate_config.getStateId(), StateDefinition.class);
                extract_plugin.addStateToExtract(output_state_def);
                }
            run_config.addPlugin(extract_plugin);
            }

        run_config.addPlugin(new TaskResultCollector(new TaskResultCollectorConfiguration()));
        for (MusePlugin plugin : _plugins)
            run_config.addPlugin(plugin);

        if (_providers.size() > 0)
            {
            run_config.addPlugin(new InjectInputsPlugin(new InjectInputsPluginConfiguration()));
            for (InputProvider provider : _providers)
                run_config.addPlugin(new InputProviderPlugin(provider));
            }
_context.raiseEvent(EndResolvingTransitionInputsEventType.create());

        run_config.withinContext(_context);  // must initialize this before we can raise any events in the context.
        BlockingThreadedTaskRunner runner = new BlockingThreadedTaskRunner(_context, run_config);
        configureMessageRelay(runner);
        // inject inputs
        runner.runTask();
        TaskResult task_result = TaskResult.find(run_config.context());
        if (task_result == null)
            return new StateTransitionResult("Unable to find the TaskResult in the TaskExecutionContext. Is is likely an internal error.");

        StateTransitionResult result;
        if (task_result.isPass())
            {
            InterTaskState output_state;
            if (extract_plugin != null)
                {
                result = new StateTransitionResult("Output states were specified, but none were extracted");
                for (StateTransitionConfiguration.TransitionOutputState tos : config.getOutputStates())
                    {
                    StateDefinition state_def = _context.getProject().getResourceStorage().getResource(tos.getStateId(), StateDefinition.class);
                    output_state = extract_plugin.getExtractedState(state_def);
                    if (output_state == null)
                        {
                        if (tos.isRequired())
                            {
                            result = new StateTransitionResult("Required output state not extracted: " + state_def.getId());
                            break;
                            }
                        }
                    else
                        {
                        if (state_def.isValid(output_state))
                            {
                            if (tos.isReplacesInput())
                                _context.getContainer().replaceState(input_state, output_state);
                            else
                                _context.getContainer().addState(output_state);
                            result = new StateTransitionResult(task_result, output_state);
                            break;
                            }
                        else if (!tos.isSkipIncomplete())
                            {
                            result = new StateTransitionResult("Output state not valid (and not skippable): " + state_def.getFirstIncompleteFieldName(output_state));
                            break;
                            }
                        }
                    }
                }
            else if (config.getInputState().isTerminate())
                {
                _context.getContainer().removeState(input_state);
                result = new StateTransitionResult(task_result, null);
                }
            else
                result = new StateTransitionResult(task_result, null);
            }
        else
            result = new StateTransitionResult("Task failed, due to: " + task_result.getSummary());

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
                }
            });
        }

    public void addPlugin(MusePlugin plugin)
        {
        _plugins.add(plugin);
        }

    public void addInputProvider(InputProvider provider)
        {
        _providers.add(provider);
        }

    private final StateTransitionContext _context;
    private final List<MusePlugin> _plugins = new ArrayList<>();
    private final List<InputProvider> _providers = new ArrayList<>();
    }