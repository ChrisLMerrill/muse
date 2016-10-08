package org.musetest.core.commandline;

import io.airlift.airline.*;
import org.musetest.core.*;
import org.musetest.core.project.*;
import org.slf4j.*;

import java.io.*;
import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public abstract class MuseCommand implements Runnable
    {
    @Option(name = "-p", description = "Path to the project folder (defaults to working directory)")
    public String project;

    @Option(name = "-O", description = "Command-line option to pass to the project, in the form name=value")
    public List<String> options;

    protected MuseProject openProject()
        {
        File project_location;
        if (project == null)
            project_location = new File(System.getProperty("user.dir"));
        else
            project_location = new File(project);
        if (!project_location.exists() || !project_location.isDirectory())
            {
            LOG.error("The project location should specify a folder (relative or absolute): " + project);
            return null;
            }

        return ProjectFactory.create(project_location, getOptions());
        }

    protected Map<String, String> getOptions()
        {
        if (_parsed_options == null)
            {
            _parsed_options = new HashMap<>();
            if (options != null)
                for (String option : options)
                    {
                    int delimiter = option.indexOf("=");
                    if (delimiter == -1)
                        _parsed_options.put(option, Boolean.TRUE.toString());
                    else
                        {
                        String name = option.substring(0, delimiter);
                        String value = option.substring(delimiter + 1);
                        _parsed_options.put(name, value);
                        }
                    }
            }
        return _parsed_options;
        }

    private Map<String, String> _parsed_options = null;

    final static Logger LOG = LoggerFactory.getLogger(MuseCommand.class);
    }


