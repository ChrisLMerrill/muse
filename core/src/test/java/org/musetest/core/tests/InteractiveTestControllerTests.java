package org.musetest.core.tests;

import org.junit.*;
import org.musetest.builtins.step.*;
import org.musetest.core.*;
import org.musetest.core.events.matching.*;
import org.musetest.core.execution.*;
import org.musetest.core.project.*;
import org.musetest.core.step.*;
import org.musetest.core.steptest.*;
import org.musetest.core.tests.utils.*;
import org.musetest.core.values.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class InteractiveTestControllerTests
    {
    /**
     * The InteractiveTestController should pause in this case, rather than stopping.
     */
    @Test
    public void testPausedOnFatalVerifyFailure()
        {
        StepConfiguration step = new StepConfiguration(Verify.TYPE_ID);
        step.addSource(Verify.CONDITION_PARAM, ValueSourceConfiguration.forValue(false)); // will cause a failure
        step.addSource(Verify.TERMINATE_PARAM, ValueSourceConfiguration.forValue(true)); // should cause test to pause
        SteppedTest test = TestTests.setupLogTest(step);

        SimpleProject project = new SimpleProject();
        InteractiveTestController controller = new InteractiveTestController();
        controller.run(new SteppedTestProviderImpl(project, test));
        TestStateBlocker blocker = new TestStateBlocker(controller);
        blocker.blockUntil(InteractiveTestState.PAUSED);

        Assert.assertEquals(InteractiveTestState.PAUSED, controller.getState());
        Assert.assertNull(controller.getResult());  // test not complete

        // should pause after verify step
        Assert.assertTrue(controller.getEventLog().findEvents(new EventTypeMatcher(MuseEventType.StartStep)).size() == 2);
        }

    /**
     * The InteractiveTestController should pause in this case, rather than stopping.
     */
    @Test
    public void testStoppedAfterError()
        {
        StepConfiguration step = new StepConfiguration(Verify.TYPE_ID);
        SteppedTest test = TestTests.setupLogTest(step);

        SimpleProject project = new SimpleProject();
        InteractiveTestController controller = new InteractiveTestController();
        controller.run(new SteppedTestProviderImpl(project, test));
        TestStateBlocker blocker = new TestStateBlocker(controller);
        blocker.blockUntil(InteractiveTestState.PAUSED);

        Assert.assertEquals(InteractiveTestState.PAUSED, controller.getState());
        Assert.assertNull(controller.getResult());  // test not considered done

        // should be stopped after verify step
        Assert.assertTrue(controller.getEventLog().findEvents(new EventTypeMatcher(MuseEventType.StartStep)).size() == 2);
        }
    }


