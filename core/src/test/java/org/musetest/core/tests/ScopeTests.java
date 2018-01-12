package org.musetest.core.tests;

import org.junit.*;
import org.musetest.core.*;
import org.musetest.core.execution.*;
import org.musetest.core.project.*;
import org.musetest.core.resource.storage.*;
import org.musetest.core.test.*;
import org.musetest.testutils.*;

import java.io.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class ScopeTests
    {
    @Test
    public void scopedGroupVariableVisibility()
        {
        runTestAndCheckForSuccess("scopedGroup");
        }

    @Test
    public void functionCallVariableScope()
        {
        runTestAndCheckForSuccess("functionScope");
        }

    @Test
    public void functionCallPassParameters()
        {
        runTestAndCheckForSuccess("functionVariablePassing");
        }

    private void runTestAndCheckForSuccess(String test_name)
        {
        File file = TestResources.getFile("projects/scopes", getClass());
        SimpleProject project = new SimpleProject(new FolderIntoMemoryResourceStorage(file));
        TestConfiguration config = new BasicTestConfiguration(test_name);
        final SimpleTestRunner runner = new SimpleTestRunner(project, config);
        runner.runTest();
        MuseTestResult result = runner.getResult();
        Assert.assertTrue(result.isPass());
        }
    }
