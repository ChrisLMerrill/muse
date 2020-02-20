package org.museautomation.core.project;

import org.museautomation.core.*;
import org.museautomation.settings.*;

import java.io.*;
import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class StepIdGenerator extends ProjectSettingsFile
    {
    public synchronized long generateLongId()
        {
        long id = _next_id;
        if (id == 0)  // never use zero as an id
            id += new Random().nextInt();
        _next_id = id + 1;
        save();
        return id;
        }

    private long _next_id = ((long) Math.abs(new Random().nextInt())) << 16;

    @SuppressWarnings("unused")  // for de/serialization
    public long getNextId()
        {
        return _next_id;
        }

    @SuppressWarnings("unused")  // for de/serialization
    public void setNextId(long next_id)
        {
        _next_id = next_id;
        }

    @SuppressWarnings("unused")  // public API
    public synchronized void conflict()
        {
        _next_id = _next_id + new Random().nextInt();
        save();
        }

    public static StepIdGenerator get(MuseProject project)
        {
        // upgrade old config
        IdGeneratorConfiguration old = IdGeneratorConfiguration.get(project);
        if (old != null)
            {
            StepIdGenerator settings = loadFromProject(StepIdGenerator.class, FILENAME, null, project);
            settings.setNextId(old.getNextId());
            project.getResourceStorage().removeResource(project.getResourceStorage().findResource(old.getId()));
            project.putProjectSettings(settings);
            return settings;
            }

        StepIdGenerator settings = project.getProjectSettings(StepIdGenerator.class);
        if (settings == null)
            {
            try
                {
                settings = loadFromProject(StepIdGenerator.class, FILENAME, null, project);
                }
            catch (Exception e)
                {
                settings = new StepIdGenerator();
                settings.setFilename(ProjectSettingsFile.getSettingsFile(FILENAME, project).getAbsolutePath());
                }
            project.putProjectSettings(settings);
            }
        return settings;
        }

    public static StepIdGenerator get(File project_folder)
        {
        return loadFromProject(StepIdGenerator.class, FILENAME, null, new File(project_folder, ".muse"));
        }

    private final static String FILENAME = "step-ids.json";
    }