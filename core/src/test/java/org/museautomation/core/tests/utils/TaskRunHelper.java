package org.museautomation.core.tests.utils;

import org.museautomation.builtins.plugins.results.*;
import org.museautomation.core.*;
import org.museautomation.core.context.*;
import org.museautomation.core.execution.*;
import org.museautomation.core.plugins.*;
import org.museautomation.core.task.*;

/**
 * Provides simple shims for running a MuseTask for unit testing.
 *
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class TaskRunHelper
    {
    /**
     * Convenience method for the simplest method of running a task and getting the result. This is useful for unit
     * tests and some command-line operations.
     *
     * @param project The project to run in
     * @param task The task to run
     *
     * @return the result
     */
    public static TaskExecutionContext runTaskReturnContext(MuseProject project, MuseTask task)
        {
        BasicTaskConfiguration config = new BasicTaskConfiguration(task);
        TaskRunner runner = new SimpleTaskRunner(new ProjectExecutionContext(project), config);
        config.addPlugin(new TaskResultCollectorConfiguration().createPlugin());
        runner.runTask();
        return runner.getExecutionContext();
        }

    public static TaskResult runTask(MuseProject project, MuseTask task)
        {
        BasicTaskConfiguration config = new BasicTaskConfiguration(task);
        TaskRunner runner = new SimpleTaskRunner(new ProjectExecutionContext(project), config);
        config.addPlugin(new TaskResultCollectorConfiguration().createPlugin());
        runner.runTask();
        return TaskResult.find(runner.getExecutionContext());
        }

    public static TaskResult runTask(MuseProject project, MuseTask task, MusePlugin plugin)
        {
        BasicTaskConfiguration config = new BasicTaskConfiguration(task);
        config.addPlugin(new TaskResultCollectorConfiguration().createPlugin());
        config.addPlugin(plugin);
        TaskRunner runner = new SimpleTaskRunner(new ProjectExecutionContext(project), config);
        runner.runTask();
        return TaskResult.find(config.context());
        }

    public static TaskResult runTask(MuseProject project, TaskConfiguration config, MusePlugin plugin)
        {
        config.addPlugin(new TaskResultCollectorConfiguration().createPlugin());
        if (plugin != null)
            config.addPlugin(plugin);
        TaskRunner runner = new SimpleTaskRunner(new ProjectExecutionContext(project), config);
        runner.runTask();
        return TaskResult.find(config.context());
        }
    }
