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
class TestRunnerTests
    {
    @Test
    void runtimeException()
        {
        MuseProject project = new SimpleProject();
        MuseTest test = new MockTestWithAction()
            {
            @Override
            protected boolean executeImplementation(TestExecutionContext context)
                {
                throw new RuntimeException("unexpected failure");
                }
            };
        TestResult result = TestRunHelper.runTest(project, test);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(TestResult.FailureType.Error, result.getFailures().get(0).getType());
        }

    }


