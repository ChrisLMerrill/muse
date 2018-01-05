package org.musetest.core.commandline;

import io.airlift.airline.*;
import org.musetest.core.*;
import org.musetest.core.format.*;
import org.slf4j.*;

import java.io.*;
import java.util.*;

/**
 * Extension to the Muse command-line launcher to run a test or test suite.
 *
 * This class is registered as a service in /META-INF/services/org.musetest.core.commandline.MuseCommand.
 *
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@Command(name = "format", description = "(re)Format a resource or file.")
public class FormatCommand extends MuseCommand
    {
    @Arguments(description = "filename/ID of resource to run", required = true)
    private String target;

    @Option(name = "-t", description = "Type of file (not needed for project resources)")
	private String type;

    @Option(name = "-f", description = "Output format")
	private String format;

    @Override
    public void run()
        {
        MuseProject project = openProject();
        Reformatters reformatters = Reformatters.get(project);
        MuseResource resource = project.getResourceStorage().getResource(target);

        if (resource == null)
	        {
	        // must be a file
	        final File input = new File(target);
	        if (input.exists())
		        {
		        List<Reformatter> list = reformatters.find(input, type, format);
		        if (list.size() > 0)
			        list.get(0).reformat(project, input, type, format, System.out);
		        else
		            System.out.println("No formatter found");
		        }
	        else
		        System.out.println(String.format("Neither file nor project resource matching '%s' could be found.", target));
	        }
        else
	        {
	        // handle the resource
	        List<Reformatter> list = reformatters.find(resource, format);
	        if (list.size() > 0)
		        list.get(0).reformat(project, resource, format, System.out);
	        else
	            LOG.error("No formatter found");
	        }
        }

    private final static Logger LOG = LoggerFactory.getLogger(FormatCommand.class);
    }


