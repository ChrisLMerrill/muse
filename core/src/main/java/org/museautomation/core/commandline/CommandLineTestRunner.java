package org.museautomation.core.commandline;

import org.museautomation.core.*;
import org.museautomation.core.context.*;
import org.museautomation.core.events.*;
import org.museautomation.core.execution.*;
import org.museautomation.core.resource.*;
import org.museautomation.core.resultstorage.*;
import org.museautomation.core.test.*;
import org.museautomation.core.test.plugins.*;
import org.slf4j.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@SuppressWarnings("unused") // invoked via reflection by RunCommand
public class CommandLineTestRunner implements MuseResourceRunner
    {
    @Override
    public boolean canRun(MuseResource resource)
        {
        return resource instanceof MuseTest;
        }

    @Override
    public boolean run(MuseProject project, MuseResource resource, boolean verbose, String output_path, String runner_id)
        {
        if (!(resource instanceof MuseTest))
            return false;

        MuseTest test = (MuseTest) resource;

        BasicTestConfiguration config = new BasicTestConfiguration(test);
        if (verbose)
            config.addPlugin(new EventLogWriterPlugin());
        if (output_path != null)
	        config.addPlugin(new VariableInitializer(SaveTestResultsToDisk.OUTPUT_FOLDER_VARIABLE_NAME, output_path));

        SimpleTestRunner runner = new SimpleTestRunner(new ProjectExecutionContext(project), config);
        runner.runTest();
        final Boolean completed = runner.completedNormally();

        final TestResult result = TestResult.find(config.context());
        String result_string = "No result was produced - try adding the 'Test Result Calculator' plugin to your project.";
        if (result != null)
            result_string = result.getSummary();

        if (!verbose)
            {
            if (completed)
                System.out.println(String.format("Test '%s' is finished: %s", config.name(), result_string));
            else
                System.out.println(String.format("Test '%s' could not be executed to completion: %s", config.name(), result_string));
            }

        return true;
        }

    private final static Logger LOG = LoggerFactory.getLogger(CommandLineTestRunner.class);
    }
