package org.musetest.core.commandline;

import io.airlift.airline.*;
import org.musetest.core.*;
import org.musetest.core.project.*;
import org.slf4j.*;

import java.io.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public abstract class MuseCommand implements Runnable
    {
    @Option(name = "-p", description = "Path to the project folder (defaults to working directory)")
    public String project;

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

        return ProjectFactory.create(project_location);
        }

    final static Logger LOG = LoggerFactory.getLogger(MuseCommand.class);
    }


