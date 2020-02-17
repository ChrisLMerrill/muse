package org.museautomation.builtins.step;

import org.jetbrains.annotations.*;
import org.junit.jupiter.api.*;
import org.museautomation.builtins.condition.*;
import org.museautomation.builtins.value.*;
import org.museautomation.core.*;
import org.museautomation.core.context.*;
import org.museautomation.core.events.*;
import org.museautomation.core.events.matching.*;
import org.museautomation.core.mocks.*;
import org.museautomation.core.project.*;
import org.museautomation.core.step.*;
import org.museautomation.core.steptask.*;
import org.museautomation.core.task.*;
import org.museautomation.core.task.plugins.*;
import org.museautomation.core.tests.utils.*;
import org.museautomation.core.values.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
class ConditionalAndLoopingTests
    {
    @Test
    void ifStepTrue() throws MuseExecutionError
        {
        StepConfiguration config = new StepConfiguration(IfStep.TYPE_ID);
        config.addSource(IfStep.CONDITION_PARAM, ValueSourceConfiguration.forValue(true));
        IfStep step = new IfStep(config, new SimpleProject());
        Assertions.assertTrue(step.shouldEnter(new MockStepExecutionContext()));
        StepExecutionResult result = step.createResult(StepExecutionStatus.COMPLETE);
        Assertions.assertEquals(Boolean.TRUE, result.metadata().getMetadataField(IfStep.IF_STEP_ENTERED));
        }

    @Test
    void ifStepFalse() throws MuseExecutionError
        {
        StepConfiguration config = new StepConfiguration(IfStep.TYPE_ID);
        config.addSource(IfStep.CONDITION_PARAM, ValueSourceConfiguration.forValue(false));
        IfStep step = new IfStep(config, new SimpleProject());
        Assertions.assertFalse(step.shouldEnter(new MockStepExecutionContext()));
        StepExecutionResult result = step.createResult(StepExecutionStatus.COMPLETE);
        Assertions.assertEquals(Boolean.FALSE, result.metadata().getMetadataField(IfStep.IF_STEP_ENTERED));
        }

    @Test
    void elseStepEntered() throws MuseExecutionError
        {
        // prepare context
        MockStepExecutionContext context = createContextWIthIfEnteredEvent(true);
        evaluateElseEntry(context, false);
        }

    @Test
    void elseStepSkipped() throws MuseExecutionError
        {
        // prepare context
        MockStepExecutionContext context = createContextWIthIfEnteredEvent(false);
        evaluateElseEntry(context, true);
        }

    @NotNull
    private MockStepExecutionContext createContextWIthIfEnteredEvent(boolean b)
        {
        StepConfiguration if_step = new StepConfiguration(IfStep.TYPE_ID);
        if_step.setMetadataField(StepConfiguration.META_ID, 111);
        MockStepExecutionContext context = new MockStepExecutionContext();
        context.getParent().getStepLocator().loadSteps(if_step);
        MuseEvent end_if_event = EndStepEventType.create(EndStepEventType.TYPE_ID, if_step);
        end_if_event.setAttribute(IfStep.IF_STEP_ENTERED, b);
        context.raiseEvent(end_if_event);
        return context;
        }

    private void evaluateElseEntry(MockStepExecutionContext context, boolean entered) throws MuseExecutionError
        {
        StepConfiguration config = new StepConfiguration(ElseStep.TYPE_ID);
        ElseStep step = new ElseStep(config, context.getProject());
        Assertions.assertEquals(entered, step.shouldEnter(context));
        }

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

        TaskResult result = runTest(new SteppedTask(main));
        Assertions.assertTrue(result.isPass());
        EventLog log = _test_config.context().getEventLog();
        Assertions.assertNotNull(log.findFirstEvent(new EventDescriptionMatcher(should_run_message)), "The conditional that should have run, did not");
        Assertions.assertNull(log.findFirstEvent(new EventDescriptionMatcher(should_not_run_message)), "The conditional that should not have run, did run");
        Assertions.assertNull(_test_config.context().getVariable("ran2"));
        }

    @Test
    void testRepeat0Times() throws MuseExecutionError
        {
        Assertions.assertFalse(willRunRepeatN(null,0));
        Assertions.assertFalse(willRunRepeatN(0L,0));
        Assertions.assertFalse(willRunRepeatN(1L,0));
        }

    @Test
    void testRepeat1Times() throws MuseExecutionError
        {
        Assertions.assertTrue(willRunRepeatN(null,1));
        Assertions.assertTrue(willRunRepeatN(0L,1));
        Assertions.assertFalse(willRunRepeatN(1L,1));
        }

    @Test
    void testRepeat3Times() throws MuseExecutionError
        {
        Assertions.assertTrue(willRunRepeatN(null,3));
        Assertions.assertTrue(willRunRepeatN(0L,3));
        Assertions.assertTrue(willRunRepeatN(1L,3));
        Assertions.assertTrue(willRunRepeatN(2L,3));
        Assertions.assertFalse(willRunRepeatN(3L,3));
        }

    private boolean willRunRepeatN(Long starting_count_value, int repeat_count) throws MuseExecutionError
        {
        MuseProject project = new SimpleProject();

        StepConfiguration loop_step = new StepConfiguration(RepeatNTimesStep.TYPE_ID);
        ValueSourceConfiguration count = ValueSourceConfiguration.forValue(repeat_count);
        loop_step.addSource(RepeatNTimesStep.COUNT_PARAM, count);

        MockStepExecutionContext context = new MockStepExecutionContext(project);
        if (starting_count_value != null)
            context.setVariable(RepeatNTimesStep.COUNT_VARNAME_DEFAULT, starting_count_value);

        RepeatNTimesStep repeat = (RepeatNTimesStep) loop_step.createStep(project);
        return repeat.shouldEnter(context);
        }

    @Test
    void testRepeatUntilOnce() throws MuseExecutionError
        {
        MuseProject project = new SimpleProject();

        StepConfiguration loop_step = new StepConfiguration(RepeatUntilStep.TYPE_ID);
        ValueSourceConfiguration count = ValueSourceConfiguration.forValue(true);
        loop_step.addSource(RepeatUntilStep.CONDITION_PARAM, count);

        MockStepExecutionContext context = new MockStepExecutionContext(project);
        RepeatUntilStep repeat = (RepeatUntilStep) loop_step.createStep(project);

        Assertions.assertTrue(repeat.shouldEnter(context));
        Assertions.assertFalse(repeat.shouldEnter((context)));
        }

    @Test
    void testRepeatUntilTwice() throws MuseExecutionError
        {
        MuseProject project = new SimpleProject();

        StepConfiguration loop_step = new StepConfiguration(RepeatUntilStep.TYPE_ID);
        ValueSourceConfiguration condition = ValueSourceConfiguration.forValue(false);
        loop_step.addSource(RepeatUntilStep.CONDITION_PARAM, condition);

        MockStepExecutionContext context = new MockStepExecutionContext(project);
        RepeatUntilStep repeat = (RepeatUntilStep) loop_step.createStep(project);

        Assertions.assertTrue(repeat.shouldEnter(context));
        Assertions.assertTrue(repeat.shouldEnter((context)));
        }

    @Test
    void testWhileStepX3()
        {
        TaskResult result = runTest(createLoopTest(0L));
        Assertions.assertTrue(result.isPass());
        EventLog log = _test_config.context().getEventLog();
        Assertions.assertNotNull(log.findFirstEvent(new EventDescriptionMatcher(MESSAGE_PREFIX + 1)), "first message is missing");
        Assertions.assertNotNull(log.findFirstEvent(new EventDescriptionMatcher(MESSAGE_PREFIX + 2)), "second message is missing");
        Assertions.assertNotNull(log.findFirstEvent(new EventDescriptionMatcher(MESSAGE_PREFIX + 3)), "third message is missing");
        Assertions.assertNull(log.findFirstEvent(new EventDescriptionMatcher(MESSAGE_PREFIX + 4)), "this message shouldn't be there");
        }

    private SteppedTask createLoopTest(Object initial_value)
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
        return new SteppedTask(main);
        }

    @Test
    void testWhileStepX1()
        {
        TaskResult result = runTest(createLoopTest(2L));
        Assertions.assertTrue(result.isPass());
        Assertions.assertNull(_test_config.context().getEventLog().findFirstEvent(new EventDescriptionMatcher(MESSAGE_PREFIX + 2)), "this should not be found");
        Assertions.assertNotNull(_test_config.context().getEventLog().findFirstEvent(new EventDescriptionMatcher(MESSAGE_PREFIX + 3)), "first message is missing");
        }

    @Test
    void testWhileStepX0()
        {
        SteppedTask test = createLoopTest(3L);
        SteppedTaskExecutionContext context = new DefaultSteppedTaskExecutionContext(new SimpleProject(), test);
        context.addPlugin(new TaskResultCollectorConfiguration().createPlugin());
        boolean finished = test.execute(context);
        Assertions.assertTrue(finished);
        TaskResult result = TaskResult.find(context);
        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.isPass());
        Assertions.assertNull(context.getEventLog().findFirstEvent(new EventDescriptionMatcher(MESSAGE_PREFIX + 1)), "this should not be found");
        }

    private TaskResult runTest(MuseTask test)
        {
        _test_config = new BasicTaskConfiguration(test);
        _test_config.addPlugin(new TaskResultCollectorConfiguration().createPlugin());
        return TaskRunHelper.runTask(new SimpleProject(), _test_config, null);
        }

    private TaskConfiguration _test_config;

    private final static String COUNTER_NAME = "counter";
    private final static String MESSAGE_PREFIX = "var=";
    }
