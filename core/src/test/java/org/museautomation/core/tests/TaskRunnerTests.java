package org.museautomation.core.tests;

import org.junit.jupiter.api.*;
import org.museautomation.core.*;
import org.museautomation.core.context.*;
import org.museautomation.core.project.*;
import org.museautomation.core.tests.mocks.*;
import org.museautomation.core.tests.utils.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
class TaskRunnerTests
    {
    @Test
    void runtimeException()
        {
        MuseProject project = new SimpleProject();
        MuseTask test = new MockTaskWithAction()
            {
            @Override
            protected boolean executeImplementation(TaskExecutionContext context)
                {
                throw new RuntimeException("unexpected failure");
                }
            };
        TaskResult result = TaskRunHelper.runTask(project, test);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(TaskResult.FailureType.Error, result.getFailures().get(0).getType());
        }

    }


