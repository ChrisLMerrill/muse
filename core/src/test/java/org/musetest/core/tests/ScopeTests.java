package org.musetest.core.tests;

import org.junit.*;
import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.helpers.*;
import org.musetest.core.project.*;
import org.musetest.core.resource.*;

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
        File file = TestUtils.getTestResource("projects/scopes", getClass());
        SimpleProject project = new SimpleProject(new FolderIntoMemoryResourceStore(file));
        MuseTest test = project.findResource(test_name, MuseTest.class);
        MuseTestResult result = test.execute(new DefaultTestExecutionContext(project));
        Assert.assertEquals(MuseTestResultStatus.Success, result.getStatus());
        }
    }


