package org.musetest.builtins.tests;

import org.junit.jupiter.api.*;
import org.musetest.builtins.condition.*;
import org.musetest.builtins.step.*;
import org.musetest.builtins.value.*;
import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.events.*;
import org.musetest.core.events.matching.*;
import org.musetest.core.project.*;
import org.musetest.core.step.*;
import org.musetest.core.steptest.*;
import org.musetest.core.test.*;
import org.musetest.core.test.plugins.*;
import org.musetest.core.tests.utils.*;
import org.musetest.core.values.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
class ConditionalAndLoopingTests
	{
	@Test
    void testIfStep()
		{
		StepConfiguration main = new StepConfiguration("compound");

		StepConfiguration will_run = new StepConfiguration(IfStep.TYPE_ID);
		ValueSourceConfiguration condition = ValueSourceConfiguration.forType(EqualityCondition.TYPE_ID);
		condition.addSource(EqualityCondition.LEFT_PARAM, ValueSourceConfiguration.forValue("ABC"));
		condition.addSource(EqualityCondition.RIGHT_PARAM, ValueSourceConfiguration.forValue("ABC"));
		will_run.addSource("condition", condition);
		StepConfiguration should_run_step = new StepConfiguration(LogMessage.TYPE_ID);
		final String should_run_message = "it ran";
		should_run_step.addSource(LogMessage.MESSAGE_PARAM, ValueSourceConfiguration.forValue(should_run_message));
		will_run.addChild(should_run_step);
		main.addChild(will_run);

		StepConfiguration wontrun = new StepConfiguration(IfStep.TYPE_ID);
		condition = ValueSourceConfiguration.forType(EqualityCondition.TYPE_ID);
		condition.addSource(EqualityCondition.LEFT_PARAM, ValueSourceConfiguration.forValue("ABC"));
		condition.addSource(EqualityCondition.RIGHT_PARAM, ValueSourceConfiguration.forValue("XYZ"));
		wontrun.addSource("condition", condition);
		StepConfiguration should_not_run_step = new StepConfiguration(LogMessage.TYPE_ID);
		final String should_not_run_message = "should not run";
		should_not_run_step.addSource(LogMessage.MESSAGE_PARAM, ValueSourceConfiguration.forValue(should_not_run_message));
		wontrun.addChild(should_not_run_step);
		main.addChild(wontrun);

		TestResult result = runTest(new SteppedTest(main));
		Assertions.assertTrue(result.isPass());
		EventLog log = _test_config.context().getEventLog();
		Assertions.assertNotNull(log.findFirstEvent(new EventDescriptionMatcher(should_run_message)), "The conditional that should have run, did not");
		Assertions.assertNull(log.findFirstEvent(new EventDescriptionMatcher(should_not_run_message)), "The conditional that should not have run, did run");
        Assertions.assertNull(_test_config.context().getVariable("ran2"));
		}

	@Test
    void testWhileStepX3()
		{
		TestResult result = runTest(createLoopTest(0L));
		Assertions.assertTrue(result.isPass());
		EventLog log = _test_config.context().getEventLog();
		Assertions.assertNotNull(log.findFirstEvent(new EventDescriptionMatcher(MESSAGE_PREFIX + 1)), "first message is missing");
		Assertions.assertNotNull(log.findFirstEvent(new EventDescriptionMatcher(MESSAGE_PREFIX + 2)), "second message is missing");
		Assertions.assertNotNull(log.findFirstEvent(new EventDescriptionMatcher(MESSAGE_PREFIX + 3)), "third message is missing");
		Assertions.assertNull(log.findFirstEvent(new EventDescriptionMatcher(MESSAGE_PREFIX + 4)), "this message shouldn't be there");
		}

	private SteppedTest createLoopTest(Object initial_value)
		{
		StepConfiguration main = new StepConfiguration("compound");

		StepConfiguration store_step = new StepConfiguration(StoreVariable.TYPE_ID);
		store_step.addSource(StoreVariable.NAME_PARAM, ValueSourceConfiguration.forValue(ConditionalAndLoopingTests.COUNTER_NAME));
		store_step.addSource(StoreVariable.VALUE_PARAM, ValueSourceConfiguration.forValue(initial_value));
		main.addChild(store_step);

		StepConfiguration while_step = new StepConfiguration(WhileStep.TYPE_ID);
		ValueSourceConfiguration condition = ValueSourceConfiguration.forType(LessThanCondition.TYPE_ID);
		condition.addSource(EqualityCondition.LEFT_PARAM, ValueSourceConfiguration.forSource(VariableValueSource.TYPE_ID, ValueSourceConfiguration.forValue(ConditionalAndLoopingTests.COUNTER_NAME)));
		condition.addSource(EqualityCondition.RIGHT_PARAM, ValueSourceConfiguration.forValue(3L));
		while_step.addSource(WhileStep.CONDITION_PARAM, condition);

		StepConfiguration increment_step = new StepConfiguration(IncrementVariable.TYPE_ID);
		increment_step.addSource(IncrementVariable.NAME_PARAM, ValueSourceConfiguration.forValue(ConditionalAndLoopingTests.COUNTER_NAME));
		while_step.addChild(increment_step);

		StepConfiguration log_step = new StepConfiguration(LogMessage.TYPE_ID);
		ValueSourceConfiguration config = ValueSourceConfiguration.forType(AdditionSource.TYPE_ID);
		config.addSource(ValueSourceConfiguration.forValue(ConditionalAndLoopingTests.MESSAGE_PREFIX));
		config.addSource(ValueSourceConfiguration.forSource(VariableValueSource.TYPE_ID, ValueSourceConfiguration.forValue(ConditionalAndLoopingTests.COUNTER_NAME)));
		log_step.addSource(LogMessage.MESSAGE_PARAM, config);
		while_step.addChild(log_step);

		main.addChild(while_step);
		return new SteppedTest(main);
		}

	@Test
    void testWhileStepX1()
		{
		TestResult result = runTest(createLoopTest(2L));
		Assertions.assertTrue(result.isPass());
		Assertions.assertNull(_test_config.context().getEventLog().findFirstEvent(new EventDescriptionMatcher(MESSAGE_PREFIX + 2)), "this should not be found");
		Assertions.assertNotNull(_test_config.context().getEventLog().findFirstEvent(new EventDescriptionMatcher(MESSAGE_PREFIX + 3)), "first message is missing");
		}

	@Test
    void testWhileStepX0()
		{
		SteppedTest test = createLoopTest(3L);
		SteppedTestExecutionContext context = new DefaultSteppedTestExecutionContext(new SimpleProject(), test);
		context.addPlugin(new TestResultCollectorConfiguration().createPlugin());
		boolean finished = test.execute(context);
		Assertions.assertTrue(finished);
		TestResult result = TestResult.find(context);
		Assertions.assertNotNull(result);
		Assertions.assertTrue(result.isPass());
		Assertions.assertNull(context.getEventLog().findFirstEvent(new EventDescriptionMatcher(MESSAGE_PREFIX + 1)), "this should not be found");
		}

	private TestResult runTest(MuseTest test)
		{
		_test_config = new BasicTestConfiguration(test);
		_test_config.addPlugin(new TestResultCollectorConfiguration().createPlugin());
		return TestRunHelper.runTest(new SimpleProject(), _test_config, null);
		}

	private TestConfiguration _test_config;

	private final static String COUNTER_NAME = "counter";
	private final static String MESSAGE_PREFIX = "var=";
	}
