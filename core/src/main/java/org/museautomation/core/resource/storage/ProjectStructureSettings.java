package org.museautomation.core.resource.storage;

import org.museautomation.core.*;
import org.museautomation.settings.*;

import java.io.*;
import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class ProjectStructureSettings extends ProjectSettingsFile
    {
    public List<String> getSubfolders()
        {
        return _subfolders;
        }

    public void setSubfolders(List<String> subfolders)
        {
        _subfolders = subfolders;
        }

    private List<String> _subfolders = new ArrayList<>();

    public static ProjectStructureSettings get(MuseProject project)
        {
        ProjectStructureSettings settings = project.getProjectSettings(ProjectStructureSettings.class);
        if (settings == null)
            {
            settings = loadFromProject(ProjectStructureSettings.class, FILENAME, null, project);
            project.putProjectSettings(settings);
            }
        return settings;
        }

    public static ProjectStructureSettings get(File project_folder)
        {
        return loadFromProject(ProjectStructureSettings.class, FILENAME, null, new File(project_folder, ".muse"));
        }

    private final static String FILENAME = "structure.json";
    }