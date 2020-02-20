package org.museautomation.core.resource.storage;

import org.junit.jupiter.api.*;
import org.museautomation.core.*;
import org.museautomation.core.project.*;

import java.io.*;
import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class ProjectSettingsTests
    {
    @Test
    public void loadProjectStructureSettings()
        {
        MuseProject project = ProjectFactory.create(new File("src/test/other_resources/projects/settings"), Collections.emptyMap());
        ProjectStructureSettings settings = ProjectStructureSettings.get(project);
        Assertions.assertNotNull(settings);
        Assertions.assertEquals(1, settings.getSubfolders().size());
        Assertions.assertEquals("functions", settings.getSubfolders().get(0));
        }
    }