package org.musetest.core.tests;

import org.junit.*;
import org.musetest.builtins.step.*;
import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.events.*;
import org.musetest.core.mocks.*;
import org.musetest.core.project.*;
import org.musetest.core.step.*;
import org.musetest.core.steptest.*;
import org.musetest.core.tests.mocks.*;

import java.util.*;
import java.util.concurrent.atomic.*;

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
        context.addInitializer(new EventLogger());
        MuseTestResult result = test.execute(context);
        Assert.assertTrue(result.isPass());

        // verify the resource was created and closed
        Assert.assertTrue("The step did not run", result.getLog().findEvents(event ->
            event.getDescription().contains(EXECUTE_MESSAGE)).size() == 1);
        MockShuttable shuttable = (MockShuttable) context.getVariable(MockStepCreatesShuttable.SHUTTABLE_VAR_NAME);
        Assert.assertNotNull(shuttable);
        Assert.assertTrue(shuttable.isShutdown());
        }

    @Test
    public void queueNewEventsDuringProcessing()
        {
        final StepConfiguration step_config = new StepConfiguration(LogMessage.TYPE_ID);
        DefaultTestExecutionContext context = new DefaultTestExecutionContext(new SimpleProject(), new SteppedTest(step_config));

        // install an event listener that raises an event in response to another event
        AtomicReference<MuseEvent> event2 = new AtomicReference<>(null);
        context.addEventListener(event ->
	        {
	        if (event.getTypeId().equals(MessageEvent.MessageEventType.TYPE_ID))  // don't go into infinite loop
		        {
		        final MockStepEvent second_event = new MockStepEvent(StepEvent.END_INSTANCE, step_config, new MockStepExecutionContext(context));
		        event2.set(second_event);
		        context.raiseEvent(second_event);
		        }
	        });

        // install an event listener to track the event ordering
        final List<MuseEvent> events = new ArrayList<>();
        context.addEventListener(events::add);

        // raise an event
        final MessageEvent event1 = new MessageEvent("message");
        context.raiseEvent(event1);

        Assert.assertEquals(2, events.size());
        Assert.assertTrue(event1 == events.get(0));
        Assert.assertTrue(event2.get() == events.get(1));
        }
    }


