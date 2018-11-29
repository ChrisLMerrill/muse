package org.musetest.core.tests;

import org.junit.jupiter.api.*;
import org.musetest.core.project.*;
import org.musetest.core.resource.storage.*;
import org.musetest.core.test.*;
import org.musetest.core.test.plugins.*;
import org.musetest.core.tests.utils.*;
import org.musetest.tests.utils.*;

import java.io.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
class ScopeTests
    {
    @Test
    void scopedGroupVariableVisibility()
        {
        runTestAndCheckForSuccess("scopedGroup");
        }

    @Test
    void functionCallVariableScope()
        {
        runTestAndCheckForSuccess("functionScope");
        }

    @Test
    void functionCallPassParameters()
        {
        runTestAndCheckForSuccess("functionVariablePassing");
        }

    private void runTestAndCheckForSuccess(String test_name)
        {
        File file = TestResources.getFile("projects/scopes", getClass());
        SimpleProject project = new SimpleProject(new FolderIntoMemoryResourceStorage(file));
        TestConfiguration config = new BasicTestConfiguration(test_name);
        Assertions.assertTrue(TestRunHelper.runTest(project, config, new TestDefaultsInitializerConfiguration().createPlugin()).isPass());
        }
    }
