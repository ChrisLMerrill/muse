package org.musetest.core.project;

import org.musetest.core.*;
import org.musetest.core.resource.*;

import java.io.*;
import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class ProjectFactory
    {
    public static MuseProject create(File folder, Map<String,String> command_line_options)
        {
        if (!folder.exists() || !folder.isDirectory())
            throw new IllegalArgumentException("'folder' parameter must be an existing directory: " + folder.getAbsolutePath());

        FolderIntoMemoryResourceStorage resources = new FolderIntoMemoryResourceStorage(folder);

        SimpleProject project = new SimpleProject(resources, folder.getName());
        project.setCommandLineOptions(command_line_options);
        return project;
        }
    }


