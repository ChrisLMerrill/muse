package org.musetest.core.tests;

import org.junit.jupiter.api.*;
import org.musetest.builtins.step.*;
import org.musetest.core.*;
import org.musetest.core.events.*;
import org.musetest.core.events.matching.*;
import org.musetest.core.step.*;
import org.musetest.core.steptest.*;
import org.musetest.core.values.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
class StepExecutionTests
    {
    @Test
    void singleStep() throws MuseExecutionError
		{
        final String message = "this is the message";
        StepConfiguration step_a = new StepConfiguration(LogMessage.TYPE_ID);
        step_a.addSource(LogMessage.MESSAGE_PARAM, ValueSourceConfiguration.forValue(message));

        MuseEvent message_event = new SimpleStepRunner(step_a).runOneStep().context().getEventLog().findFirstEvent(new EventDescriptionMatcher(message));
        Assertions.assertNotNull(message_event, "message step did not run");
        }

    @Test
    void singleNestedStep() throws MuseExecutionError
		{
        StepConfiguration parent = new StepConfiguration();
        parent.setType("compound");

        final String message = "the message";
        StepConfiguration child = new StepConfiguration(LogMessage.TYPE_ID);
        child.addSource(LogMessage.MESSAGE_PARAM, ValueSourceConfiguration.forValue(message));
        parent.addChild(child);

        MuseEvent message_event = new SimpleStepRunner(parent).runAll().eventLog().findFirstEvent(new EventDescriptionMatcher(message));
        Assertions.assertNotNull(message_event, "step didn't run");
        }

    @Test
    void twoNestedSteps() throws MuseExecutionError
		{
        StepConfiguration parent = new StepConfiguration();
        parent.setType("compound");

        final String message1 = "message1";
        StepConfiguration child1 = new StepConfiguration(LogMessage.TYPE_ID);
        child1.addSource(LogMessage.MESSAGE_PARAM, ValueSourceConfiguration.forValue(message1));
        parent.addChild(child1);

        final String message2 = "message2";
        StepConfiguration child2 = new StepConfiguration(LogMessage.TYPE_ID);
        child2.addSource(LogMessage.MESSAGE_PARAM, ValueSourceConfiguration.forValue(message2));
        parent.addChild(child2);

		EventLog log = new SimpleStepRunner(parent).runAll().eventLog();
        Assertions.assertNotNull(log.findFirstEvent(new EventDescriptionMatcher(message1)), "first step didn't run");
        Assertions.assertNotNull(log.findFirstEvent(new EventDescriptionMatcher(message2)), "second step didn't run");
        }

    @Test
    void stepMissingParameter() throws MuseExecutionError
		{
        StepConfiguration step_a = new StepConfiguration();
        step_a.setType("blahblah");

        SimpleStepRunner runner = new SimpleStepRunner(step_a);
        runner.context().addEventListener(new TerminateOnError(runner.executor()));
        EventLog log = runner.runOneStep().eventLog();
        Assertions.assertNotNull(log.findFirstEvent(new EventTypeMatcher(StartStepEventType.TYPE_ID)), "step didn't start");
        Assertions.assertNotNull(log.findFirstEvent(new StepResultStatusMatcher(StepExecutionStatus.ERROR)), "step should have failed");
        }

    @Test
    void stepParameterResolvesToNull() throws MuseExecutionError
		{
        StepConfiguration step_a = new StepConfiguration();
        step_a.setType("blahblah");
        step_a.addSource(StoreVariable.NAME_PARAM, ValueSourceConfiguration.forValue(null));

        SimpleStepRunner runner = new SimpleStepRunner(step_a);
        runner.context().addEventListener(new TerminateOnError(runner.executor()));
        EventLog log = runner.runOneStep().eventLog();
		Assertions.assertNotNull(log.findFirstEvent(new EventTypeMatcher(StartStepEventType.TYPE_ID)), "step didn't start");
        Assertions.assertNotNull(log.findFirstEvent(new StepResultStatusMatcher(StepExecutionStatus.ERROR)), "step should have failed");
        }
    }
