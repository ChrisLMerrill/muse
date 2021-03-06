package org.museautomation.core.commandline;

import io.airlift.airline.*;
import org.museautomation.core.*;
import org.museautomation.core.resource.*;
import org.slf4j.*;

import java.util.*;

/**
 * Extension to the Muse command-line launcher to run a test or test suite.
 *
 * This class is registered as a service in /META-INF/services/org.museautomation.core.commandline.MuseCommand.
 *
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@Command(name = "run", description = "Run a project resource (test, test suite, ...)")
public class RunCommand extends MuseCommand
    {
    @Arguments(description = "filename/ID of resource to run", required = true)
    private String resource_id;

    @Option(name = "-v", description = "Verbose output")
	private boolean verbose;

    @Option(name = "-o", description = "Output path")
	private String output_path;

    @Option(name = "-r", description = "Runner configuration id")
    private String runner_id;

    @Override
    public void run()
        {
        MuseProject project = openProject();
        MuseResource resource = project.getResourceStorage().getResource(resource_id);
        String id = resource_id;
        while (resource == null && id.contains("."))
            {
            id = id.substring(0, id.lastIndexOf("."));
            resource = project.getResourceStorage().getResource(id);
            }
        if (resource == null)
            {
            LOG.error(String.format("Resource with id %s was not found in the project.", resource_id));
            return;
            }

        // find an runner for this resource and run it.
        List<MuseResourceRunner> possible_runners = project.getClassLocator().getInstances(MuseResourceRunner.class);
        for (MuseResourceRunner runner : possible_runners)
            {
            if (runner.canRun(resource))
                {
                if (runner.run(project, resource, verbose, output_path, runner_id))
                    return;
                else
                    System.exit(1);
                }
            }

        LOG.error(String.format("No editor found for resource type %s (id=%s).", resource.getType().getName(), resource_id));
        }

    private final static Logger LOG = LoggerFactory.getLogger(RunCommand.class);
    }


