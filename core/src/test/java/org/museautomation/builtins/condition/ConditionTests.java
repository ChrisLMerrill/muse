package org.museautomation.builtins.condition;

import org.junit.jupiter.api.*;
import org.museautomation.builtins.tests.mocks.*;
import org.museautomation.builtins.value.*;
import org.museautomation.core.*;
import org.museautomation.core.context.*;
import org.museautomation.core.mocks.*;
import org.museautomation.core.project.*;
import org.museautomation.core.resource.*;
import org.museautomation.core.values.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
class ConditionTests
    {
    @Test
    void listContainsSource() throws MuseInstantiationException, ValueSourceResolutionError
        {
        ValueSourceConfiguration config = ValueSourceConfiguration.forType(ListContainsSource.TYPE_ID);
        List<String> list = new ArrayList<>();
        list.add("abc");
        list.add("def");
        config.addSource(ListContainsSource.LIST_PARAM, ValueSourceConfiguration.forTypeWithValue(MockValueSource.TYPE_ID, list));
        SimpleProject project = new SimpleProject();

        config.addSource(ListContainsSource.TARGET_PARAM, ValueSourceConfiguration.forValue("xyz"));
        MuseValueSource source = config.createSource(project);
        Assertions.assertFalse((boolean) source.resolveValue(new ProjectExecutionContext(project)));

        config.replaceSource(ListContainsSource.TARGET_PARAM, ValueSourceConfiguration.forValue("abc"));
        source = config.createSource(project);
        Assertions.assertTrue((boolean) source.resolveValue(new ProjectExecutionContext(project)));
        }

    @Test
    void stringContainsSource() throws MuseInstantiationException, ValueSourceResolutionError
        {
        ValueSourceConfiguration config = ValueSourceConfiguration.forType(StringContainsSource.TYPE_ID);
        config.addSource(StringContainsSource.STRING_PARAM, ValueSourceConfiguration.forValue("the little brown fox"));
        SimpleProject project = new SimpleProject();

        config.addSource(StringContainsSource.TARGET_PARAM, ValueSourceConfiguration.forValue("abc"));
        MuseValueSource source = config.createSource(project);
        Assertions.assertFalse((boolean) source.resolveValue(new ProjectExecutionContext(project)));

        config.replaceSource(StringContainsSource.TARGET_PARAM, ValueSourceConfiguration.forValue("brown"));
        source = config.createSource(project);
        Assertions.assertTrue((boolean) source.resolveValue(new ProjectExecutionContext(project)));
        }

    @Test
    void globMatchCondition() throws MuseInstantiationException, ValueSourceResolutionError
        {
        ValueSourceConfiguration matcher = ValueSourceConfiguration.forType(GlobMatchCondition.TYPE_ID);
        matcher.addSource(GlobMatchCondition.PATTERN_PARAM, ValueSourceConfiguration.forValue("a*z"));
        matcher.addSource(GlobMatchCondition.TARGET_PARAM, ValueSourceConfiguration.forValue("a2z"));
        MuseValueSource source = matcher.createSource();
        Boolean result = (Boolean) source.resolveValue(new MockStepExecutionContext());
        Assertions.assertTrue(result);
        }

    @Test
    void regexMatchCondition() throws MuseInstantiationException, ValueSourceResolutionError
        {
        ValueSourceConfiguration matcher = ValueSourceConfiguration.forType(RegexMatchCondition.TYPE_ID);
        matcher.addSource(RegexMatchCondition.PATTERN_PARAM, ValueSourceConfiguration.forValue("a.*z"));
        matcher.addSource(RegexMatchCondition.TARGET_PARAM, ValueSourceConfiguration.forValue("a2z"));
        MuseValueSource source = matcher.createSource();
        Boolean result = (Boolean) source.resolveValue(new MockStepExecutionContext());
        Assertions.assertTrue(result);
        }

    @Test
    void regexMatchCaseInsensitive() throws MuseInstantiationException, ValueSourceResolutionError
        {
        ValueSourceConfiguration matcher = ValueSourceConfiguration.forType(RegexMatchCondition.TYPE_ID);
        matcher.addSource(RegexMatchCondition.PATTERN_PARAM, ValueSourceConfiguration.forValue("a.*z"));
        matcher.addSource(RegexMatchCondition.TARGET_PARAM, ValueSourceConfiguration.forValue("a2Z"));
        matcher.addSource(RegexMatchCondition.CASE_PARAM, ValueSourceConfiguration.forValue(true));
        MuseValueSource source = matcher.createSource();
        Boolean result = (Boolean) source.resolveValue(new MockStepExecutionContext());
        Assertions.assertTrue(result);
        }

    @Test
    void stringEquality() throws MuseExecutionError
        {
        StepExecutionContext context = new MockStepExecutionContext();

        ValueSourceConfiguration condition = createCondition(EqualityCondition.TYPE_ID, ValueSourceConfiguration.forValue("abc"), ValueSourceConfiguration.forValue("abc"));
        Assertions.assertTrue((Boolean) condition.createSource().resolveValue(context));

        condition = createCondition(EqualityCondition.TYPE_ID, ValueSourceConfiguration.forValue("abc"), ValueSourceConfiguration.forValue("xyz"));
        Assertions.assertFalse((Boolean) condition.createSource().resolveValue(context));
        }

    @Test
    void variableEquality() throws MuseExecutionError
        {
        StepExecutionContext context = new MockStepExecutionContext();

        context.setVariable("var1", "abc");

        ValueSourceConfiguration left = ValueSourceConfiguration.forValue("abc");
        ValueSourceConfiguration right = ValueSourceConfiguration.forSource(VariableValueSource.TYPE_ID, ValueSourceConfiguration.forValue("var1"));
        ValueSourceConfiguration condition = createCondition(EqualityCondition.TYPE_ID, left, right);
        Assertions.assertTrue((Boolean) condition.createSource().resolveValue(context));

        left = ValueSourceConfiguration.forValue("xyz");
        condition = createCondition(EqualityCondition.TYPE_ID, left, right);
        Assertions.assertFalse((Boolean) condition.createSource().resolveValue(context));

        context.setVariable("var2", "abc");
        left = ValueSourceConfiguration.forSource(VariableValueSource.TYPE_ID, ValueSourceConfiguration.forValue("var1"));
        right = ValueSourceConfiguration.forSource(VariableValueSource.TYPE_ID, ValueSourceConfiguration.forValue("var2"));
        condition = createCondition(EqualityCondition.TYPE_ID, left, right);
        Assertions.assertTrue((Boolean) condition.createSource().resolveValue(context));

        context.setVariable("var2", "xyz");
        Assertions.assertFalse((Boolean) condition.createSource().resolveValue(context));
        }

    @Test
    void integerGreaterThan() throws MuseExecutionError
        {
        StepExecutionContext context = new MockStepExecutionContext();
        ValueSourceConfiguration condition;

        // compare two constants
        condition = createCondition(GreaterThanCondition.TYPE_ID, ValueSourceConfiguration.forValue(1L), ValueSourceConfiguration.forValue(0L));
        Assertions.assertTrue((Boolean) condition.createSource().resolveValue(context));

        condition = createCondition(GreaterThanCondition.TYPE_ID, ValueSourceConfiguration.forValue(0L), ValueSourceConfiguration.forValue(1L));
        Assertions.assertFalse((Boolean) condition.createSource().resolveValue(context));

        condition = createCondition(GreaterThanCondition.TYPE_ID, ValueSourceConfiguration.forValue(0L), ValueSourceConfiguration.forValue(0L));
        Assertions.assertFalse((Boolean) condition.createSource().resolveValue(context));

        // compare variables
        context.setVariable("var1", 1L);
        context.setVariable("var2", 0L);

        ValueSourceConfiguration var1 = ValueSourceConfiguration.forSource(VariableValueSource.TYPE_ID, ValueSourceConfiguration.forValue("var1"));
        ValueSourceConfiguration var2 = ValueSourceConfiguration.forSource(VariableValueSource.TYPE_ID, ValueSourceConfiguration.forValue("var2"));

        condition = createCondition(GreaterThanCondition.TYPE_ID, var1, var2);
        Assertions.assertTrue((Boolean) condition.createSource().resolveValue(context));

        condition = createCondition(GreaterThanCondition.TYPE_ID, var2, var1);
        Assertions.assertFalse((Boolean) condition.createSource().resolveValue(context));

        condition = createCondition(GreaterThanCondition.TYPE_ID, var2, var2);
        Assertions.assertFalse((Boolean) condition.createSource().resolveValue(context));
        }

    @Test
    void testStringGreaterThan() throws MuseExecutionError
        {
        StepExecutionContext context = new MockStepExecutionContext();
        ValueSourceConfiguration condition;

        // compare two constants
        // compare two constants
        condition = createCondition(GreaterThanCondition.TYPE_ID, ValueSourceConfiguration.forValue("aab"), ValueSourceConfiguration.forValue("aaa"));
        Assertions.assertTrue((Boolean) condition.createSource().resolveValue(context));

        condition = createCondition(GreaterThanCondition.TYPE_ID, ValueSourceConfiguration.forValue("aaa"), ValueSourceConfiguration.forValue("aab"));
        Assertions.assertFalse((Boolean) condition.createSource().resolveValue(context));

        condition = createCondition(GreaterThanCondition.TYPE_ID, ValueSourceConfiguration.forValue("x"), ValueSourceConfiguration.forValue("x"));
        Assertions.assertFalse((Boolean) condition.createSource().resolveValue(context));

        // compare variables
        context.setVariable("var1", "aab");
        context.setVariable("var2", "aaa");

        ValueSourceConfiguration var1 = ValueSourceConfiguration.forSource(VariableValueSource.TYPE_ID, ValueSourceConfiguration.forValue("var1"));
        ValueSourceConfiguration var2 = ValueSourceConfiguration.forSource(VariableValueSource.TYPE_ID, ValueSourceConfiguration.forValue("var2"));

        condition = createCondition(GreaterThanCondition.TYPE_ID, var1, var2);
        Assertions.assertTrue((Boolean) condition.createSource().resolveValue(context));

        condition = createCondition(GreaterThanCondition.TYPE_ID, var2, var1);
        Assertions.assertFalse((Boolean) condition.createSource().resolveValue(context));

        condition = createCondition(GreaterThanCondition.TYPE_ID, var2, var2);
        Assertions.assertFalse((Boolean) condition.createSource().resolveValue(context));
        }

    @Test
    void integerLessThan() throws MuseExecutionError
        {
        StepExecutionContext context = new MockStepExecutionContext();
        ValueSourceConfiguration condition;

        // compare two constants
        condition = createCondition(LessThanCondition.TYPE_ID, ValueSourceConfiguration.forValue(0L), ValueSourceConfiguration.forValue(1L));
        Assertions.assertTrue((Boolean) condition.createSource().resolveValue(context));

        condition = createCondition(LessThanCondition.TYPE_ID, ValueSourceConfiguration.forValue(1L), ValueSourceConfiguration.forValue(0L));
        Assertions.assertFalse((Boolean) condition.createSource().resolveValue(context));

        condition = createCondition(LessThanCondition.TYPE_ID, ValueSourceConfiguration.forValue(1L), ValueSourceConfiguration.forValue(1L));
        Assertions.assertFalse((Boolean) condition.createSource().resolveValue(context));

        // compare variables
        context.setVariable("var1", 0L);
        context.setVariable("var2", 1L);

        ValueSourceConfiguration var1 = ValueSourceConfiguration.forSource(VariableValueSource.TYPE_ID, ValueSourceConfiguration.forValue("var1"));
        ValueSourceConfiguration var2 = ValueSourceConfiguration.forSource(VariableValueSource.TYPE_ID, ValueSourceConfiguration.forValue("var2"));

        condition = createCondition(LessThanCondition.TYPE_ID, var1, var2);
        Assertions.assertTrue((Boolean) condition.createSource().resolveValue(context));

        condition = createCondition(LessThanCondition.TYPE_ID, var2, var1);
        Assertions.assertFalse((Boolean) condition.createSource().resolveValue(context));

        condition = createCondition(LessThanCondition.TYPE_ID, var2, var2);
        Assertions.assertFalse((Boolean) condition.createSource().resolveValue(context));
        }

    @Test
    void stringLessThan() throws MuseExecutionError
        {
        StepExecutionContext context = new MockStepExecutionContext();
        ValueSourceConfiguration condition;

        // compare two constants
        condition = createCondition(LessThanCondition.TYPE_ID, ValueSourceConfiguration.forValue("aaa"), ValueSourceConfiguration.forValue("aab"));
        Assertions.assertTrue((Boolean) condition.createSource().resolveValue(context));

        condition = createCondition(LessThanCondition.TYPE_ID, ValueSourceConfiguration.forValue("aab"), ValueSourceConfiguration.forValue("aaa"));
        Assertions.assertFalse((Boolean) condition.createSource().resolveValue(context));

        condition = createCondition(LessThanCondition.TYPE_ID, ValueSourceConfiguration.forValue("x"), ValueSourceConfiguration.forValue("x"));
        Assertions.assertFalse((Boolean) condition.createSource().resolveValue(context));

        // compare variables
        context.setVariable("var1", "aaa");
        context.setVariable("var2", "aab");

        ValueSourceConfiguration var1 = ValueSourceConfiguration.forSource(VariableValueSource.TYPE_ID, ValueSourceConfiguration.forValue("var1"));
        ValueSourceConfiguration var2 = ValueSourceConfiguration.forSource(VariableValueSource.TYPE_ID, ValueSourceConfiguration.forValue("var2"));

        condition = createCondition(LessThanCondition.TYPE_ID, var1, var2);
        Assertions.assertTrue((Boolean) condition.createSource().resolveValue(context));

        condition = createCondition(LessThanCondition.TYPE_ID, var2, var1);
        Assertions.assertFalse((Boolean) condition.createSource().resolveValue(context));

        condition = createCondition(LessThanCondition.TYPE_ID, var2, var2);
        Assertions.assertFalse((Boolean) condition.createSource().resolveValue(context));
        }

    @Test
    void integerDoubleEquality() throws MuseExecutionError
        {
        StepExecutionContext context = new MockStepExecutionContext();
        ValueSourceConfiguration condition = createCondition(EqualityCondition.TYPE_ID, ValueSourceConfiguration.forValue(123L), ValueSourceConfiguration.forValue(123.0d));
        Assertions.assertTrue((Boolean) condition.createSource().resolveValue(context));
        }

    private ValueSourceConfiguration createCondition(String type, ValueSourceConfiguration left, ValueSourceConfiguration right)
        {
        ValueSourceConfiguration config = ValueSourceConfiguration.forType(type);
        config.addSource(BinaryCondition.LEFT_PARAM, left);
        config.addSource(BinaryCondition.RIGHT_PARAM, right);
        return config;
        }

    }
