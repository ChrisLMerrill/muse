package org.musetest.core.tests;

import org.junit.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Test;
import org.musetest.core.*;
import org.musetest.core.project.*;
import org.musetest.core.tests.utils.*;

import java.io.*;
import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class ProjectClasspathTests
    {
    @Test
    public void testStepLoadedFromProjectClassesFolder()
        {
        MuseProject project = ProjectFactory.create(new File("src/test/other_resources/projects/classpath"), Collections.emptyMap());
        project.open();

        // was the project able to load resources using classes from the project classpath?
        MuseTest test = project.getResourceStorage().getResource("TestUsesCustomJavaStep", MuseTest.class);
        Assertions.assertNotNull(test);

        // Running test directly may pass or fail, depending on when the FactoryLocator scanned the classpath.
        // E.g. in a unit test scenario, it has probably already happened (unless this class is the only test run)
        // so it would fail. But running it through the project ensures it will have the correct classpath.
        TestResult result = TestRunHelper.runTest(project, test);
        Assertions.assertTrue(result.isPass());
        }

    @Test
    public void testStepLoadedFromProjectJar()
        {
        MuseProject project = ProjectFactory.create(new File("src/test/other_resources/projects/classpath"), Collections.emptyMap());
        project.open();

        // was the project able to load resources using classes from the project classpath?
        MuseTest test = project.getResourceStorage().getResource("TestUsesCustomJavaStepFromJar", MuseTest.class);
        Assertions.assertNotNull(test);

        // Running test directly may pass or fail, depending on when the FactoryLocator scanned the classpath.
        // E.g. in a unit test scenario, it has probably already happened (unless this class is the only test run)
        // so it would fail. But running it through the project ensures it will have the correct classpath.
        TestResult result = TestRunHelper.runTest(project, test);
        Assertions.assertTrue(result.isPass());
        }
    }


