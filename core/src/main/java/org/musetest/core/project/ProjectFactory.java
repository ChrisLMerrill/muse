package org.musetest.core.project;

import org.musetest.core.*;
import org.musetest.core.resource.*;
import org.slf4j.*;

import java.io.*;
import java.lang.reflect.*;
import java.net.*;
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

        FolderIntoMemoryResourceStore resources = new FolderIntoMemoryResourceStore(folder);

        SimpleProject project = new SimpleProject(resources);
        project.setCommandLineOptions(command_line_options);
        return project;
        }
    }


