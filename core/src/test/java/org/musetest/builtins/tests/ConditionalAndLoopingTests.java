package org.musetest.builtins.tests;

import org.junit.*;
import org.musetest.builtins.condition.*;
import org.musetest.builtins.step.*;
import org.musetest.builtins.value.*;
import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.step.*;
import org.musetest.core.steptest.*;
import org.musetest.core.values.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class ConditionalAndLoopingTests
    {
    @Test
    public void testIfStep()
        {
        StepConfiguration main = new StepConfiguration("compound");

        StepConfiguration will_run = new StepConfiguration(IfStep.TYPE_ID);
        ValueSourceConfiguration condition = ValueSourceConfiguration.forType(EqualityCondition.TYPE_ID);
        condition.addSource(EqualityCondition.LEFT_PARAM, ValueSourceConfiguration.forValue("ABC"));
        condition.addSource(EqualityCondition.RIGHT_PARAM, ValueSourceConfiguration.forValue("ABC"));
        will_run.addSource("condition", condition);
        StepConfiguration store_ran1 = new StepConfiguration(StoreVariable.TYPE_ID);
        store_ran1.addSource(StoreVariable.NAME_PARAM, ValueSourceConfiguration.forValue("ran1"));
        store_ran1.addSource(StoreVariable.VALUE_PARAM, ValueSourceConfiguration.forValue(true));
        will_run.addChild(store_ran1);
        main.addChild(will_run);

        StepConfiguration wontrun = new StepConfiguration(IfStep.TYPE_ID);
        condition = ValueSourceConfiguration.forType(EqualityCondition.TYPE_ID);
        condition.addSource(EqualityCondition.LEFT_PARAM, ValueSourceConfiguration.forValue("ABC"));
        condition.addSource(EqualityCondition.RIGHT_PARAM, ValueSourceConfiguration.forValue("XYZ"));
        wontrun.addSource("condition", condition);
        StepConfiguration store_ran2 = new StepConfiguration(StoreVariable.TYPE_ID);
        store_ran2.addSource(StoreVariable.NAME_PARAM, ValueSourceConfiguration.forValue("ran2"));
        store_ran2.addSource(StoreVariable.VALUE_PARAM, ValueSourceConfiguration.forValue(true));
        wontrun.addChild(store_ran2);
        main.addChild(wontrun);

        TestExecutionContext context = new DefaultTestExecutionContext();
        SteppedTest test = new SteppedTest(main);
        MuseTestResult result = test.execute(context);
        Assert.assertEquals(MuseTestResultStatus.Success, result.getStatus());
        Assert.assertEquals(true, context.getVariable("ran1"));
        Assert.assertEquals(null, context.getVariable("ran2"));
        }

    @Test
    public void testWhileStepX3()
        {
        String counter_var_name = "counter";

        SteppedTest test = createLoopTest(COUNTER_NAME, MESSAGE_PREFIX);

        TestExecutionContext context = new DefaultTestExecutionContext();
        context.setVariable(counter_var_name, 0L);
        MuseTestResult result = test.execute(context);
        Assert.assertEquals(MuseTestResultStatus.Success, result.getStatus());
        Assert.assertEquals(3L, context.getVariable(counter_var_name));
        }

    private SteppedTest createLoopTest(String counter_var_name, String message_prefix)
        {
        StepConfiguration main = new StepConfiguration("compound");

        StepConfiguration while_step = new StepConfiguration(WhileStep.TYPE_ID);
        ValueSourceConfiguration condition = ValueSourceConfiguration.forType(LessThanCondition.TYPE_ID);
        condition.addSource(EqualityCondition.LEFT_PARAM, ValueSourceConfiguration.forSource(VariableValueSource.TYPE_ID, ValueSourceConfiguration.forValue(counter_var_name)));
        condition.addSource(EqualityCondition.RIGHT_PARAM, ValueSourceConfiguration.forValue(3L));
        while_step.addSource(WhileStep.CONDITION_PARAM, condition);

        StepConfiguration increment_step = new StepConfiguration(IncrementVariable.TYPE_ID);
        increment_step.addSource(IncrementVariable.NAME_PARAM, ValueSourceConfiguration.forValue(counter_var_name));
        while_step.addChild(increment_step);

        StepConfiguration log_step = new StepConfiguration(LogMessage.TYPE_ID);
        ValueSourceConfiguration config = ValueSourceConfiguration.forType(StringConcatenationSource.TYPE_ID);
        config.addSource(ValueSourceConfiguration.forValue(message_prefix));
        config.addSource(ValueSourceConfiguration.forSource(VariableValueSource.TYPE_ID, ValueSourceConfiguration.forValue(counter_var_name)));
        log_step.addSource(LogMessage.MESSAGE_PARAM, config);
        while_step.addChild(log_step);

        main.addChild(while_step);
        return new SteppedTest(main);
        }

    @Test
    public void testWhileStepX1()
        {
        String counter_var_name = "counter";

        SteppedTest test = createLoopTest(COUNTER_NAME, MESSAGE_PREFIX);

        TestExecutionContext context = new DefaultTestExecutionContext();
        context.setVariable(counter_var_name, 2L);
        MuseTestResult result = test.execute(context);
        Assert.assertEquals(MuseTestResultStatus.Success, result.getStatus());
        Assert.assertEquals(3L, context.getVariable(counter_var_name));
        }

    @Test
    public void testWhileStepX0()
        {
        String counter_var_name = "counter";

        SteppedTest test = createLoopTest(COUNTER_NAME, MESSAGE_PREFIX);

        TestExecutionContext context = new DefaultTestExecutionContext();
        context.setVariable(counter_var_name, 3L);
        MuseTestResult result = test.execute(context);
        Assert.assertEquals(MuseTestResultStatus.Success, result.getStatus());
        Assert.assertEquals(3L, context.getVariable(counter_var_name));
        }

    private final static String COUNTER_NAME = "counter";
    private final static String MESSAGE_PREFIX = "var=";
    }


