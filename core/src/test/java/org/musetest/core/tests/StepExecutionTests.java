package org.musetest.core.tests;

import org.junit.jupiter.api.*;
import org.musetest.builtins.step.*;
import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.events.*;
import org.musetest.core.events.matching.*;
import org.musetest.core.project.*;
import org.musetest.core.step.*;
import org.musetest.core.steptest.*;
import org.musetest.core.values.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class StepExecutionTests
    {
    @Test
    public void singleStep() throws MuseExecutionError
		{
        final String message = "this is the message";
        StepConfiguration step_a = new StepConfiguration(LogMessage.TYPE_ID);
        step_a.addSource(LogMessage.MESSAGE_PARAM, ValueSourceConfiguration.forValue(message));

        SteppedTest test = new SteppedTest(step_a);
		SteppedTestExecutionContext test_context = new DefaultSteppedTestExecutionContext(new SimpleProject(), test);
		test_context.initializePlugins();

		StepExecutor executor = new StepExecutor(test, test_context);
        executor.executeAll();

		//noinspection ConstantConditions
        Assertions.assertNotNull(test_context.getEventLog().findFirstEvent(new EventDescriptionMatcher(message)), "message step did not run");
        }

    @Test
    public void singleNestedStep() throws MuseExecutionError
		{
        StepConfiguration parent = new StepConfiguration();
        parent.setType("compound");

        final String message = "the message";
        StepConfiguration child = new StepConfiguration(LogMessage.TYPE_ID);
        child.addSource(LogMessage.MESSAGE_PARAM, ValueSourceConfiguration.forValue(message));
        parent.addChild(child);

        SteppedTest test = new SteppedTest(parent);
        SteppedTestExecutionContext test_context = new DefaultSteppedTestExecutionContext(new SimpleProject(), test);
		test_context.initializePlugins();

        StepExecutor executor = new StepExecutor(test, test_context);
        executor.executeAll();

		//noinspection ConstantConditions
        Assertions.assertNotNull(test_context.getEventLog().findFirstEvent(new EventDescriptionMatcher(message)), "step didn't run");
        }

    @Test
    public void twoNestedSteps() throws MuseExecutionError
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

        SteppedTest test = new SteppedTest(parent);
        SteppedTestExecutionContext test_context = new DefaultSteppedTestExecutionContext(new SimpleProject(), test);
		test_context.initializePlugins();

        StepExecutor executor = new StepExecutor(test, test_context);
        executor.executeAll();

		EventLog log = test_context.getEventLog();
		//noinspection ConstantConditions
        Assertions.assertNotNull(log.findFirstEvent(new EventDescriptionMatcher(message1)), "first step didn't run");
        Assertions.assertNotNull(log.findFirstEvent(new EventDescriptionMatcher(message2)), "second step didn't run");
        }

    @Test
    public void stepMissingParameter() throws MuseExecutionError
		{
        StepConfiguration step_a = new StepConfiguration();
        step_a.setType("blahblah");
        SteppedTest test = new SteppedTest(step_a);
		SteppedTestExecutionContext test_context = new DefaultSteppedTestExecutionContext(new SimpleProject(), test);
		test_context.initializePlugins();

		StepExecutor executor = new StepExecutor(test, test_context);
        test_context.addEventListener(new TerminateOnError(executor));
        executor.executeAll();

		EventLog log = test_context.getEventLog();
		//noinspection ConstantConditions
        Assertions.assertNotNull(log.findFirstEvent(new EventTypeMatcher(StartStepEventType.TYPE_ID)), "step didn't start");
        Assertions.assertNotNull(log.findFirstEvent(new StepResultStatusMatcher(StepExecutionStatus.ERROR)), "step should have failed");
        }

    @Test
    public void stepParameterResolvesToNull() throws MuseExecutionError
		{
        StepConfiguration step_a = new StepConfiguration();
        step_a.setType("blahblah");
        step_a.addSource(StoreVariable.NAME_PARAM, ValueSourceConfiguration.forValue(null));

        SteppedTest test = new SteppedTest(step_a);
		SteppedTestExecutionContext test_context = new DefaultSteppedTestExecutionContext(new SimpleProject(), test);
		test_context.initializePlugins();

		StepExecutor executor = new StepExecutor(test, test_context);
        test_context.addEventListener(new TerminateOnError(executor));
        executor.executeAll();

		EventLog log = test_context.getEventLog();
		//noinspection ConstantConditions
		Assertions.assertNotNull(log.findFirstEvent(new EventTypeMatcher(StartStepEventType.TYPE_ID)), "step didn't start");
        Assertions.assertNotNull(log.findFirstEvent(new StepResultStatusMatcher(StepExecutionStatus.ERROR)), "step should have failed");
        }
    }
