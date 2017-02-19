package org.musetest.core.tests;

import org.junit.*;
import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.execution.*;
import org.musetest.core.project.*;
import org.musetest.core.tests.mocks.*;
import org.musetest.core.variables.*;

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
            protected MuseTestResult executeImplementation(TestExecutionContext context)
                {
                throw new RuntimeException("unexpected failure");
                }
            };
        TestRunner runner = new SimpleTestRunner(project, test);

        runner.runTest();
        MuseTestResult result = runner.getResult();

        Assert.assertNotNull(result);
        Assert.assertEquals(MuseTestFailureDescription.FailureType.Error, result.getFailureDescription().getFailureType());
        }

    }


