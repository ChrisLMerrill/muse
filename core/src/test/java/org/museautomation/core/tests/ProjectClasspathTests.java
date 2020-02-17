package org.museautomation.core.tests;

import org.junit.jupiter.api.*;
import org.museautomation.core.*;
import org.museautomation.core.project.*;
import org.museautomation.core.tests.utils.*;

import java.io.*;
import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
class ProjectClasspathTests
    {
    @Test
    void testStepLoadedFromProjectClassesFolder()
        {
        MuseProject project = ProjectFactory.create(new File("src/test/other_resources/projects/classpath"), Collections.emptyMap());
        project.open();

        // was the project able to load resources using classes from the project classpath?
        MuseTask test = project.getResourceStorage().getResource("TestUsesCustomJavaStep", MuseTask.class);
        Assertions.assertNotNull(test);

        // Running test directly may pass or fail, depending on when the FactoryLocator scanned the classpath.
        // E.g. in a unit test scenario, it has probably already happened (unless this class is the only test run)
        // so it would fail. But running it through the project ensures it will have the correct classpath.
        TaskResult result = TaskRunHelper.runTask(project, test);
        Assertions.assertTrue(result.isPass());
        }

    @Test
    void testStepLoadedFromProjectJar()
        {
        MuseProject project = ProjectFactory.create(new File("src/test/other_resources/projects/classpath"), Collections.emptyMap());
        project.open();

        // was the project able to load resources using classes from the project classpath?
        MuseTask test = project.getResourceStorage().getResource("TestUsesCustomJavaStepFromJar", MuseTask.class);
        Assertions.assertNotNull(test);

        // Running test directly may pass or fail, depending on when the FactoryLocator scanned the classpath.
        // E.g. in a unit test scenario, it has probably already happened (unless this class is the only test run)
        // so it would fail. But running it through the project ensures it will have the correct classpath.
        TaskResult result = TaskRunHelper.runTask(project, test);
        Assertions.assertTrue(result.isPass());
        }
    }


