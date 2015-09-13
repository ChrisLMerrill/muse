package org.musetest.builtins.tests;

import org.junit.*;
import org.musetest.core.*;
import org.musetest.core.execution.*;
import org.musetest.core.project.*;

import java.io.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class ProjectClasspathTests
    {
    @Test
    public void testStepLoadedFromProjectClassesFolder() throws ClassNotFoundException
        {
        MuseProject project = ProjectFactory.create(new File("src/test/other_resources/projects/classpath"));
        project.open();

        // was the project able to load resources using classes from the project classpath?
        MuseTest test = project.findResource("TestUsesCustomJavaStep", MuseTest.class);
        Assert.assertNotNull(test);

        // Running test directly may pass or fail, depending on when the FactoryLocator scanned the classpath.
        // E.g. in a unit test scenario, it has probably already happened (unless this class is the only test run)
        // so it would fail. But running it through the project ensures it will have the correct classpath.
        MuseTestResult result = TestRunnerFactory.runTest(project, test);
        Assert.assertEquals(MuseTestResultStatus.Success, result.getStatus());
        }

    @Test
    public void testStepLoadedFromProjectJar() throws ClassNotFoundException
        {
        MuseProject project = ProjectFactory.create(new File("src/test/other_resources/projects/classpath"));
        project.open();

        // was the project able to load resources using classes from the project classpath?
        MuseTest test = project.findResource("TestUsesCustomJavaStepFromJar", MuseTest.class);
        Assert.assertNotNull(test);

        // Running test directly may pass or fail, depending on when the FactoryLocator scanned the classpath.
        // E.g. in a unit test scenario, it has probably already happened (unless this class is the only test run)
        // so it would fail. But running it through the project ensures it will have the correct classpath.
        MuseTestResult result = TestRunnerFactory.runTest(project, test);
        Assert.assertEquals(MuseTestResultStatus.Success, result.getStatus());
        }
    }


