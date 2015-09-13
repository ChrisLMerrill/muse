package org.musetest.core.project;

import org.musetest.core.*;
import org.musetest.core.resource.*;
import org.slf4j.*;

import java.io.*;
import java.lang.reflect.*;
import java.net.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class ProjectFactory
    {
    public static MuseProject create(File folder)
        {
        if (!folder.exists() || !folder.isDirectory())
            throw new IllegalArgumentException("'folder' parameter must be an existing directory: " + folder.getAbsolutePath());

        FolderIntoMemoryResourceStore resources = new FolderIntoMemoryResourceStore(folder);

        return new SimpleProject(resources);
        }
    }


