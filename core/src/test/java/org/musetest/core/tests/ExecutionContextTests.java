package org.musetest.core.tests;

import org.junit.*;
import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.events.*;
import org.musetest.core.project.*;
import org.musetest.core.step.*;
import org.musetest.core.steptest.*;
import org.musetest.core.tests.mocks.*;

import static org.musetest.core.tests.mocks.MockStepCreatesShuttable.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class ExecutionContextTests
    {
    @Test
    public void shutShuttables()
        {
        // create a test with a step that creates a Shuttable resource
        MuseProject project = new SimpleProject();
        StepConfiguration step = new StepConfiguration(MockStepCreatesShuttable.TYPE_ID);
        SteppedTest test = new SteppedTest(step);

        // run the test
        DefaultTestExecutionContext context = new DefaultTestExecutionContext(project, test);
        context.addInitializer(new EventLog());
        MuseTestResult result = test.execute(context);
        Assert.assertTrue(result.isPass());

        // verify the resource was created and closed
        Assert.assertTrue("The step did not run", result.getLog().findEvents(event ->
            event.getDescription().contains(EXECUTE_MESSAGE)).size() == 1);
        MockShuttable shuttable = (MockShuttable) context.getVariable(MockStepCreatesShuttable.SHUTTABLE_VAR_NAME);
        Assert.assertNotNull(shuttable);
        Assert.assertTrue(shuttable.isShutdown());
        }
    }


