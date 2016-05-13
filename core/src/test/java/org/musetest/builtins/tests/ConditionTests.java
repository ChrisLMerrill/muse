package org.musetest.builtins.tests;

import org.junit.*;
import org.musetest.builtins.condition.*;
import org.musetest.builtins.value.*;
import org.musetest.core.context.*;
import org.musetest.core.mocks.*;
import org.musetest.core.steptest.*;
import org.musetest.core.values.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class ConditionTests
    {
    @Test
    public void stringEquality() throws StepExecutionError
        {
        StepExecutionContext context = new DummyStepExecutionContext();

        ValueSourceConfiguration condition = createCondition(EqualityCondition.TYPE_ID, ValueSourceConfiguration.forValue("abc"), ValueSourceConfiguration.forValue("abc"));
        Assert.assertTrue((Boolean) condition.createSource().resolveValue(context));

        condition = createCondition(EqualityCondition.TYPE_ID, ValueSourceConfiguration.forValue("abc"), ValueSourceConfiguration.forValue("xyz"));
        Assert.assertFalse((Boolean) condition.createSource().resolveValue(context));
        }

    @Test
    public void variableEquality() throws StepExecutionError
        {
        StepExecutionContext context = new DummyStepExecutionContext();

        context.setLocalVariable("var1", "abc");

        ValueSourceConfiguration left = ValueSourceConfiguration.forValue("abc");
        ValueSourceConfiguration right = ValueSourceConfiguration.forSource(VariableValueSource.TYPE_ID, ValueSourceConfiguration.forValue("var1"));
        ValueSourceConfiguration condition = createCondition(EqualityCondition.TYPE_ID, left, right);
        Assert.assertTrue((Boolean) condition.createSource().resolveValue(context));

        left = ValueSourceConfiguration.forValue("xyz");
        condition = createCondition(EqualityCondition.TYPE_ID, left, right);
        Assert.assertFalse((Boolean) condition.createSource().resolveValue(context));

        context.setLocalVariable("var2", "abc");
        left = ValueSourceConfiguration.forSource(VariableValueSource.TYPE_ID, ValueSourceConfiguration.forValue("var1"));
        right = ValueSourceConfiguration.forSource(VariableValueSource.TYPE_ID, ValueSourceConfiguration.forValue("var2"));
        condition = createCondition(EqualityCondition.TYPE_ID, left, right);
        Assert.assertTrue((Boolean) condition.createSource().resolveValue(context));

        context.setLocalVariable("var2", "xyz");
        Assert.assertFalse((Boolean) condition.createSource().resolveValue(context));
        }

    @Test
    public void integerGreaterThan() throws StepExecutionError
        {
        StepExecutionContext context = new DummyStepExecutionContext();
        ValueSourceConfiguration condition;

        // compare two constants
        condition = createCondition(GreaterThanCondition.TYPE_ID, ValueSourceConfiguration.forValue(1L), ValueSourceConfiguration.forValue(0L));
        Assert.assertTrue((Boolean) condition.createSource().resolveValue(context));

        condition = createCondition(GreaterThanCondition.TYPE_ID, ValueSourceConfiguration.forValue(0L), ValueSourceConfiguration.forValue(1L));
        Assert.assertFalse((Boolean) condition.createSource().resolveValue(context));

        condition = createCondition(GreaterThanCondition.TYPE_ID, ValueSourceConfiguration.forValue(0L), ValueSourceConfiguration.forValue(0L));
        Assert.assertFalse((Boolean) condition.createSource().resolveValue(context));

        // compare variables
        context.setLocalVariable("var1", 1L);
        context.setLocalVariable("var2", 0L);

        ValueSourceConfiguration var1 = ValueSourceConfiguration.forSource(VariableValueSource.TYPE_ID, ValueSourceConfiguration.forValue("var1"));
        ValueSourceConfiguration var2 = ValueSourceConfiguration.forSource(VariableValueSource.TYPE_ID, ValueSourceConfiguration.forValue("var2"));

        condition = createCondition(GreaterThanCondition.TYPE_ID, var1, var2);
        Assert.assertTrue((Boolean) condition.createSource().resolveValue(context));

        condition = createCondition(GreaterThanCondition.TYPE_ID, var2, var1);
        Assert.assertFalse((Boolean) condition.createSource().resolveValue(context));

        condition = createCondition(GreaterThanCondition.TYPE_ID, var2, var2);
        Assert.assertFalse((Boolean) condition.createSource().resolveValue(context));
        }

    @Test
    public void testStringGreaterThan() throws StepExecutionError
        {
        StepExecutionContext context = new DummyStepExecutionContext();
        ValueSourceConfiguration condition;

        // compare two constants
        // compare two constants
        condition = createCondition(GreaterThanCondition.TYPE_ID, ValueSourceConfiguration.forValue("aab"), ValueSourceConfiguration.forValue("aaa"));
        Assert.assertTrue((Boolean) condition.createSource().resolveValue(context));

        condition = createCondition(GreaterThanCondition.TYPE_ID, ValueSourceConfiguration.forValue("aaa"), ValueSourceConfiguration.forValue("aab"));
        Assert.assertFalse((Boolean) condition.createSource().resolveValue(context));

        condition = createCondition(GreaterThanCondition.TYPE_ID, ValueSourceConfiguration.forValue("x"), ValueSourceConfiguration.forValue("x"));
        Assert.assertFalse((Boolean) condition.createSource().resolveValue(context));

        // compare variables
        context.setLocalVariable("var1", "aab");
        context.setLocalVariable("var2", "aaa");

        ValueSourceConfiguration var1 = ValueSourceConfiguration.forSource(VariableValueSource.TYPE_ID, ValueSourceConfiguration.forValue("var1"));
        ValueSourceConfiguration var2 = ValueSourceConfiguration.forSource(VariableValueSource.TYPE_ID, ValueSourceConfiguration.forValue("var2"));

        condition = createCondition(GreaterThanCondition.TYPE_ID, var1, var2);
        Assert.assertTrue((Boolean) condition.createSource().resolveValue(context));

        condition = createCondition(GreaterThanCondition.TYPE_ID, var2, var1);
        Assert.assertFalse((Boolean) condition.createSource().resolveValue(context));

        condition = createCondition(GreaterThanCondition.TYPE_ID, var2, var2);
        Assert.assertFalse((Boolean) condition.createSource().resolveValue(context));
        }

    @Test
    public void integerLessThan() throws StepExecutionError
        {
        StepExecutionContext context = new DummyStepExecutionContext();
        ValueSourceConfiguration condition;

        // compare two constants
        condition = createCondition(LessThanCondition.TYPE_ID, ValueSourceConfiguration.forValue(0L), ValueSourceConfiguration.forValue(1L));
        Assert.assertTrue((Boolean) condition.createSource().resolveValue(context));

        condition = createCondition(LessThanCondition.TYPE_ID, ValueSourceConfiguration.forValue(1L), ValueSourceConfiguration.forValue(0L));
        Assert.assertFalse((Boolean) condition.createSource().resolveValue(context));

        condition = createCondition(LessThanCondition.TYPE_ID, ValueSourceConfiguration.forValue(1L), ValueSourceConfiguration.forValue(1L));
        Assert.assertFalse((Boolean) condition.createSource().resolveValue(context));

        // compare variables
        context.setLocalVariable("var1", 0L);
        context.setLocalVariable("var2", 1L);

        ValueSourceConfiguration var1 = ValueSourceConfiguration.forSource(VariableValueSource.TYPE_ID, ValueSourceConfiguration.forValue("var1"));
        ValueSourceConfiguration var2 = ValueSourceConfiguration.forSource(VariableValueSource.TYPE_ID, ValueSourceConfiguration.forValue("var2"));

        condition = createCondition(LessThanCondition.TYPE_ID, var1, var2);
        Assert.assertTrue((Boolean) condition.createSource().resolveValue(context));

        condition = createCondition(LessThanCondition.TYPE_ID, var2, var1);
        Assert.assertFalse((Boolean) condition.createSource().resolveValue(context));

        condition = createCondition(LessThanCondition.TYPE_ID, var2, var2);
        Assert.assertFalse((Boolean) condition.createSource().resolveValue(context));
        }

    @Test
    public void stringLessThan() throws StepExecutionError
        {
        StepExecutionContext context = new DummyStepExecutionContext();
        ValueSourceConfiguration condition;

        // compare two constants
        condition = createCondition(LessThanCondition.TYPE_ID, ValueSourceConfiguration.forValue("aaa"), ValueSourceConfiguration.forValue("aab"));
        Assert.assertTrue((Boolean) condition.createSource().resolveValue(context));

        condition = createCondition(LessThanCondition.TYPE_ID, ValueSourceConfiguration.forValue("aab"), ValueSourceConfiguration.forValue("aaa"));
        Assert.assertFalse((Boolean) condition.createSource().resolveValue(context));

        condition = createCondition(LessThanCondition.TYPE_ID, ValueSourceConfiguration.forValue("x"), ValueSourceConfiguration.forValue("x"));
        Assert.assertFalse((Boolean) condition.createSource().resolveValue(context));

        // compare variables
        context.setLocalVariable("var1", "aaa");
        context.setLocalVariable("var2", "aab");

        ValueSourceConfiguration var1 = ValueSourceConfiguration.forSource(VariableValueSource.TYPE_ID, ValueSourceConfiguration.forValue("var1"));
        ValueSourceConfiguration var2 = ValueSourceConfiguration.forSource(VariableValueSource.TYPE_ID, ValueSourceConfiguration.forValue("var2"));

        condition = createCondition(LessThanCondition.TYPE_ID, var1, var2);
        Assert.assertTrue((Boolean) condition.createSource().resolveValue(context));

        condition = createCondition(LessThanCondition.TYPE_ID, var2, var1);
        Assert.assertFalse((Boolean) condition.createSource().resolveValue(context));

        condition = createCondition(LessThanCondition.TYPE_ID, var2, var2);
        Assert.assertFalse((Boolean) condition.createSource().resolveValue(context));
        }

    @Test
    public void integerDoubleEquality() throws StepExecutionError
        {
        StepExecutionContext context = new DummyStepExecutionContext();
        ValueSourceConfiguration condition = createCondition(EqualityCondition.TYPE_ID, ValueSourceConfiguration.forValue(123L), ValueSourceConfiguration.forValue(123.0d));
        Assert.assertTrue((Boolean) condition.createSource().resolveValue(context));
        }

    private ValueSourceConfiguration createCondition(String type, ValueSourceConfiguration left, ValueSourceConfiguration right)
        {
        ValueSourceConfiguration config = ValueSourceConfiguration.forType(type);
        config.addSource(BinaryCondition.LEFT_PARAM, left);
        config.addSource(BinaryCondition.RIGHT_PARAM, right);
        return config;
        }
    }


