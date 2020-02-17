package org.museautomation.core.commandline;

import org.museautomation.core.*;
import org.museautomation.core.resource.*;
import org.museautomation.core.suite.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@SuppressWarnings("unused") // invoked via reflection by RunCommand
public class CommandLineTaskSuiteRunner implements MuseResourceRunner
    {
    @Override
    public boolean canRun(MuseResource resource)
        {
        return resource instanceof MuseTaskSuite;
        }

    @Override
    public boolean run(MuseProject project, MuseResource resource, boolean verbose, String output_path, String runner_id)
        {
        if (!(resource instanceof MuseTaskSuite))
            return false;
        MuseTaskSuite suite = (MuseTaskSuite) resource;

        try
            {
            MuseTaskSuiteRunner runner = new SimpleTaskSuiteRunner();
            if (runner_id != null)
	            {
	            // lookup the runner specified
	            final ResourceToken token = project.getResourceStorage().findResource(runner_id);
	            if (token == null)
		            {
		            System.out.println("runner configuration with id=" + runner_id + " was not found in the project.");
		            return false;
		            }
	            if (!(token.getResource() instanceof SuiteRunnerConfiguration))
		            {
		            System.out.println("resource with id=" + runner_id + " is not a TaskSuiteRunner.");
		            return false;
		            }
	            SuiteRunnerConfiguration config = (SuiteRunnerConfiguration) token.getResource();
	            runner = config.createRunner(new DefaultTaskSuiteExecutionContext(project, suite));
	            }

            if (output_path != null)
            	runner.setOutputPath(output_path);

            boolean success = runner.execute(project, suite, Collections.emptyList());
            if (!success)
	            System.out.println("Some tasks could not be executed. See muse.log and the output files for more information.");
            }
        catch (Exception e)
            {
            System.err.println("Unable to run task suite " + suite.getId());
            e.printStackTrace(System.err);
            return false;
            }
        return true;
        }
    }
