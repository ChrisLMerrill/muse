package org.museautomation.settings;

import com.fasterxml.jackson.databind.*;
import org.museautomation.core.*;
import org.museautomation.core.resource.*;
import org.museautomation.core.resource.storage.*;
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

    protected static boolean exists(String filename, MuseProject project)
        {
        return getSettingsFile(filename, project).exists();
        }

    protected static File getSettingsFile(String filename, MuseProject project)
        {
        return new File(getSettingsFolder(project), filename);
        }

    protected static File getSettingsFolder(MuseProject project)
        {
        ResourceStorage storage = project.getResourceStorage();
        if (storage instanceof FolderIntoMemoryResourceStorage)
            return new File(((FolderIntoMemoryResourceStorage)storage).getBaseLocation(), ".muse");
        else
            throw new UnsupportedOperationException("settings files are not supported in this type of ResourceStorage: " + storage.getClass().getSimpleName());
        }

    protected static <T extends ProjectSettingsFile> T loadFromProject(Class<T> type, String filename, ObjectMapper mapper, MuseProject project)
        {
        return BaseSettingsFile.load(type, filename, mapper, getSettingsFolder(project));
        }
    }