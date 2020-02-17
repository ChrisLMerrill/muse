package org.museautomation.core.commandline;

import org.museautomation.core.*;
import org.museautomation.core.context.*;
import org.museautomation.core.events.*;
import org.museautomation.core.execution.*;
import org.museautomation.core.resource.*;
import org.museautomation.core.resultstorage.*;
import org.museautomation.core.task.*;
import org.museautomation.core.task.plugins.*;
import org.slf4j.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@SuppressWarnings("unused") // invoked via reflection by RunCommand
public class CommandLineTaskRunner implements MuseResourceRunner
    {
    @Override
    public boolean canRun(MuseResource resource)
        {
        return resource instanceof MuseTask;
        }

    @Override
    public boolean run(MuseProject project, MuseResource resource, boolean verbose, String output_path, String runner_id)
        {
        if (!(resource instanceof MuseTask))
            return false;

        MuseTask task = (MuseTask) resource;

        BasicTaskConfiguration config = new BasicTaskConfiguration(task);
        if (verbose)
            config.addPlugin(new EventLogWriterPlugin());
        if (output_path != null)
	        config.addPlugin(new VariableInitializer(SaveTaskResultsToDisk.OUTPUT_FOLDER_VARIABLE_NAME, output_path));

        SimpleTaskRunner runner = new SimpleTaskRunner(new ProjectExecutionContext(project), config);
        runner.runTask();
        final Boolean completed = runner.completedNormally();

        final TaskResult result = TaskResult.find(config.context());
        String result_string = "No result was produced - try adding the 'Task Result Calculator' plugin to your project.";
        if (result != null)
            result_string = result.getSummary();

        if (!verbose)
            {
            if (completed)
                System.out.println(String.format("Task '%s' is finished: %s", config.name(), result_string));
            else
                System.out.println(String.format("Task '%s' could not be executed to completion: %s", config.name(), result_string));
            }

        return true;
        }

    private final static Logger LOG = LoggerFactory.getLogger(CommandLineTaskRunner.class);
    }
