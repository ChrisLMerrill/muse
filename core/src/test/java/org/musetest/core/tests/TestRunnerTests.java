package org.musetest.core.tests;

import org.junit.*;
import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.project.*;
import org.musetest.core.tests.mocks.*;
import org.musetest.core.tests.utils.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class TestRunnerTests
    {
    @Test
    public void runtimeException()
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
        Assert.assertNotNull(result);
        Assert.assertEquals(TestResult.FailureType.Error, result.getFailures().get(0).getType());
        }

    }


