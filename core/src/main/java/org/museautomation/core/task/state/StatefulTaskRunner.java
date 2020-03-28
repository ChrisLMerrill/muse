package org.museautomation.core.task.state;

import org.museautomation.builtins.plugins.input.*;
import org.museautomation.builtins.plugins.state.*;
import org.museautomation.core.*;
import org.museautomation.core.context.*;
import org.museautomation.core.execution.*;
import org.museautomation.core.plugins.*;
import org.museautomation.core.task.*;

import java.util.*;

/**
 * Runs tasks in the context of a StateContainer and InputProviders
 *
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@SuppressWarnings("unused")
public class StatefulTaskRunner
    {
    public StatefulTaskRunner(MuseProject project)
        {
        _project = project;
        }

    public StatefulTaskResult runTask(MuseTask task, InputProvider provider)
        {
        // prep run configuration and state plugins
        TaskConfiguration run_config = new BasicTaskConfiguration(task);
        for (MusePlugin plugin : _plugins)
            run_config.addPlugin(plugin);
        if (provider != null)
            run_config.addPlugin(new InputProviderPlugin(provider));
        run_config.addPlugin(new InjectInputsPlugin(new InjectInputsPluginConfiguration()));
        run_config.addPlugin(new InjectStatePlugin(new InjectStatePluginConfiguration()));
        run_config.addPlugin(new ExtractStatePlugin(new ExtractStatePluginConfiguration()));
        run_config.addPlugin(new StateContainerPlugin(_states));
        
        BlockingThreadedTaskRunner runner = new BlockingThreadedTaskRunner(new ProjectExecutionContext(_project), run_config);
        runner.runTask();

        TaskResult task_result = TaskResult.find(runner.getExecutionContext());
        if (task_result == null)
            return new StatefulTaskResult("failed - unable to find the result!");
        if (task_result.isPass())
            // TODO verify the state was extracted
            return new StatefulTaskResult();
        else
            return new StatefulTaskResult("failed");
        }

    public StateContainer getStateContainer()
        {
        return _states;
        }

    public void setStateContainer(StateContainer container)
        {
        _states = container;
        }

    public void addPlugin(MusePlugin plugin)
        {
        _plugins.add(plugin);
        }

    private MuseProject _project;
    private List<MusePlugin> _plugins = new ArrayList<>();
    private StateContainer _states = new BasicStateContainer();

    public static class StatefulTaskResult
        {
        StatefulTaskResult()
            {
            _passed = true;
            _message = "task completed successfully";
            }

        StatefulTaskResult(String message)
            {
            _passed = false;
            _message = message;
            }

        public StatefulTaskResult(boolean passed, String message)
            {
            _passed = passed;
            _message = message;
            }

        public boolean passed()
            {
            return _passed;
            }

        public boolean failed()
            {
            return !_passed;
            }

        public String getMessage()
            {
            return _message;
            }

        @Override
        public String toString()
            {
            return _message;
            }

        private final String _message;
        private final boolean _passed;
        }
    }