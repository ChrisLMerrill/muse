package org.museautomation.core.project;

import org.museautomation.core.*;
import org.museautomation.core.resource.storage.*;

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


