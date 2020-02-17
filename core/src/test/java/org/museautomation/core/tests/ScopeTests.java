package org.museautomation.core.tests;

import org.junit.jupiter.api.*;
import org.museautomation.builtins.plugins.init.*;
import org.museautomation.core.project.*;
import org.museautomation.core.resource.storage.*;
import org.museautomation.core.task.*;
import org.museautomation.core.tests.utils.*;
import org.museautomation.utils.*;

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
        TaskConfiguration config = new BasicTaskConfiguration(test_name);
        Assertions.assertTrue(TaskRunHelper.runTask(project, config, new TaskDefaultsInitializerConfiguration().createPlugin()).isPass());
        }
    }
