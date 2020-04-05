package org.museautomation.core.task.state;

import org.museautomation.builtins.plugins.input.*;
import org.museautomation.builtins.plugins.results.*;
import org.museautomation.builtins.plugins.state.*;
import org.museautomation.core.*;
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
        StateTransitionConfiguration config = _context.getConfig();
        ResourceToken<MuseResource> token = _context.getProject().getResourceStorage().findResource(config.getTaskId());
        if (token == null)
            return new StateTransitionResult(String.format("Cannot execute transition. The specified task id (%s) was not found.", config.getTaskId()));
        MuseResource resource = token.getResource();
        if (!new MuseTask.TaskResourceType().equals(resource.getType()))
            return new StateTransitionResult(String.format("Cannot execute transition. The specified task id (%s) is not a task. It is a %s.", config.getTaskId(), resource.getType()));

        MuseTask task = (MuseTask) resource;
        TaskConfiguration run_config = new BasicTaskConfiguration(task);

        // setup state injector
        InjectStatePlugin inject_plugin = new InjectStatePlugin(new InjectStatePluginConfiguration());
        inject_plugin.addState(_context.getInputState());
        run_config.addPlugin(inject_plugin);

        // setup state extractor
        ExtractStatePlugin extract_plugin = null;
        if (config.getOutputState() != null)
            {
            extract_plugin = new ExtractStatePlugin(new ExtractStatePluginConfiguration());
            StateDefinition output_state_def = _context.getProject().getResourceStorage().getResource(config.getOutputState().getStateId(), StateDefinition.class);
            extract_plugin.addStateToExtract(output_state_def);
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

        BlockingThreadedTaskRunner runner = new BlockingThreadedTaskRunner(_context, run_config);
        runner.runTask();

        TaskResult task_result = TaskResult.find(runner.getExecutionContext());
        if (task_result == null)
            return new StateTransitionResult("Unable to find the TaskResult in the TaskExecutionContext. Is is likely an internal error.");
        if (task_result.isPass())
            {
            InterTaskState output_state = null;
            if (extract_plugin != null)
                {
                output_state = extract_plugin.getExtractedStates().get(0);
                if (output_state == null)
                    return new StateTransitionResult("Output state not extracted: " + config.getOutputState().getStateId());
                _context.getContainer().addState(output_state);
                return new StateTransitionResult(task_result, output_state);
                }
            return new StateTransitionResult(task_result, output_state);
            }
        else
            return new StateTransitionResult("Task failed, due to: " + task_result.getSummary());
        }

    public void addPlugin(MusePlugin plugin)
        {
        _plugins.add(plugin);
        }

    public void addInputProvider(InputProvider provider)
        {
        _providers.add(provider);
        }

    private StateTransitionContext _context;
    private List<MusePlugin> _plugins = new ArrayList<>();
    private List<InputProvider> _providers = new ArrayList<>();
    }