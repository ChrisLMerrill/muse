package org.musetest.core.resource.storage;

import org.musetest.core.*;
import org.musetest.settings.*;

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
        return loadFromProject(ProjectStructureSettings.class, FILENAME, null, project);
        }

    public static ProjectStructureSettings get(File project_folder)
        {
        return loadFromProject(ProjectStructureSettings.class, FILENAME, null, new File(project_folder, ".muse"));
        }

    private final static String FILENAME = "structure.json";
    }