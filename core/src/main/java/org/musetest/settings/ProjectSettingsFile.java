package org.musetest.settings;

import com.fasterxml.jackson.databind.*;
import org.musetest.core.*;
import org.musetest.core.resource.*;
import org.musetest.core.resource.storage.*;
import org.slf4j.*;

import java.io.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class ProjectSettingsFile extends BaseSettingsFile
    {
    protected static <T extends ProjectSettingsFile> T loadFromProject(Class<T> type, String filename, ObjectMapper mapper, File folder)
        {
        return BaseSettingsFile.load(type, filename, mapper, folder);
        }

    protected static <T extends ProjectSettingsFile> T loadFromProject(Class<T> type, String filename, ObjectMapper mapper, MuseProject project)
        {
        ResourceStorage storage = project.getResourceStorage();
        if (storage instanceof FolderIntoMemoryResourceStorage)
            {
            File folder = new File(((FolderIntoMemoryResourceStorage)storage).getBaseLocation(), ".muse");
            return BaseSettingsFile.load(type, filename, mapper, folder);
            }
        else
            {
            LOG.warn("Trying to load a project settings file, but project is not the right type: " + project.getClass().getSimpleName());
            return null;
            }
        }

    private final static Logger LOG = LoggerFactory.getLogger(ProjectSettingsFile.class);
    }