package org.musetest.builtins.tests;

import org.junit.jupiter.api.*;
import org.musetest.builtins.condition.*;
import org.musetest.builtins.step.*;
import org.musetest.builtins.tests.mocks.*;
import org.musetest.builtins.value.*;
import org.musetest.builtins.value.collection.*;
import org.musetest.builtins.value.property.*;
import org.musetest.builtins.value.sysvar.*;
import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.mocks.*;
import org.musetest.core.project.*;
import org.musetest.core.resource.*;
import org.musetest.core.resource.storage.*;
import org.musetest.core.step.*;
import org.musetest.core.steptest.*;
import org.musetest.core.values.*;
import org.musetest.tests.utils.*;

import java.io.*;
import java.text.*;
import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
class ValueSourceTests
    {
    @Test
    void stringConstantSource() throws MuseExecutionError
        {
        ValueSourceConfiguration config = ValueSourceConfiguration.forValue("abc");
        MuseValueSource source = config.createSource();
        Assertions.assertEquals("abc", source.resolveValue(null).toString());
        }

    @Test
    void integerConstantSource() throws MuseExecutionError
        {
        MuseValueSource source = ValueSourceConfiguration.forValue(456L).createSource();
        Object value = source.resolveValue(null);
        Assertions.assertTrue(value instanceof Long);
        Assertions.assertEquals(456L, value);
        }

    @Test
    void booleanConstantSource() throws MuseExecutionError
        {
        Assertions.assertTrue((Boolean) ValueSourceConfiguration.forValue(true).createSource().resolveValue(null));
        Assertions.assertFalse((Boolean) ValueSourceConfiguration.forValue(false).createSource().resolveValue(null));
        }

    @Test
    void variableValueSource() throws MuseExecutionError
        {
        MuseValueSource source = ValueSourceConfiguration.forSource(VariableValueSource.TYPE_ID, ValueSourceConfiguration.forValue("abc")).createSource();
        StepExecutionContext context = new MockStepExecutionContext();
        context.setVariable("abc", 123L);
        Assertions.assertEquals(123L, source.resolveValue(context));
        }

    @Test
    void variableValueSourceWithVariableName() throws MuseExecutionError
        {
        ValueSourceConfiguration varname_holding_the_varname = ValueSourceConfiguration.forSource(VariableValueSource.TYPE_ID, ValueSourceConfiguration.forValue("varname"));
        MuseValueSource source = ValueSourceConfiguration.forSource(VariableValueSource.TYPE_ID, varname_holding_the_varname).createSource();
        StepExecutionContext context = new MockStepExecutionContext();
        context.setVariable("abc", 123L);
        context.setVariable("varname", "abc");
        Assertions.assertEquals(123L, source.resolveValue(context));
        }

    @Test
    void stringConcatenation() throws MuseExecutionError
        {
        ValueSourceConfiguration config = ValueSourceConfiguration.forType(AdditionSource.TYPE_ID);
        config.addSource(ValueSourceConfiguration.forValue("abc"));
        config.addSource(ValueSourceConfiguration.forValue("lmnop"));
        config.addSource(ValueSourceConfiguration.forValue("xyz"));
        MuseValueSource source = config.createSource(new SimpleProject());
        Assertions.assertEquals("abclmnopxyz", source.resolveValue(new MockStepExecutionContext()));
        }

    @Test
    void mixedConcatenation() throws MuseExecutionError
        {
        ValueSourceConfiguration config = ValueSourceConfiguration.forType(AdditionSource.TYPE_ID);
        config.addSource(ValueSourceConfiguration.forValue("abc"));
        config.addSource(ValueSourceConfiguration.forValue(8L));
        MuseValueSource source = config.createSource(new SimpleProject());
        Assertions.assertEquals("abc8", source.resolveValue(new MockStepExecutionContext()));
        }

    @Test
    void additionOfNumbers() throws MuseExecutionError
        {
        ValueSourceConfiguration config = ValueSourceConfiguration.forType(AdditionSource.TYPE_ID);
        config.addSource(ValueSourceConfiguration.forValue(1L));
        config.addSource(ValueSourceConfiguration.forValue(8L));
        MuseValueSource source = config.createSource(new SimpleProject());
        Assertions.assertEquals(9L, source.resolveValue(new MockStepExecutionContext()));
        }

    @Test
    void projectResourceValueSource() throws MuseExecutionError, IOException
        {
        final String test_id = "test_id";
        MuseTest test = new MockTest(test_id);
        MuseProject project = new SimpleProject(new InMemoryResourceStorage());
        project.getResourceStorage().addResource(test);

        ValueSourceConfiguration config = ValueSourceConfiguration.forType(ProjectResourceValueSource.TYPE_ID);
        config.setSource(ValueSourceConfiguration.forValue(test_id));
        MuseValueSource source = config.createSource(project);
        Assertions.assertTrue(source instanceof ProjectResourceValueSource);

        Object resolved = source.resolveValue(new MockStepExecutionContext(project));
        Assertions.assertEquals(test, resolved);
        }

    @Test
    void missingSubsourceProjectResourceValueSource()
        {
        MuseProject project = new SimpleProject(new InMemoryResourceStorage());
        ValueSourceConfiguration config = ValueSourceConfiguration.forType(ProjectResourceValueSource.TYPE_ID);

        MuseInstantiationException error = null;
        try
            {
            MuseValueSource source = config.createSource(project);
            Assertions.assertTrue(source instanceof ProjectResourceValueSource);
            }
        catch (MuseInstantiationException e)
            {
            error = e;
            }
        Assertions.assertNotNull(error);
        }

    @Test
    void misconfiguredProjectResourceValueSource() throws MuseInstantiationException
        {
        MuseProject project = new SimpleProject(new InMemoryResourceStorage());
        ValueSourceConfiguration config = ValueSourceConfiguration.forType(ProjectResourceValueSource.TYPE_ID);
        config.setSource(ValueSourceConfiguration.forValue(null));
        MuseValueSource source = config.createSource(project);
        Assertions.assertTrue(source instanceof ProjectResourceValueSource);

        MuseExecutionError error = null;
        try
            {
            source.resolveValue(new MockStepExecutionContext());
            }
        catch (MuseExecutionError e)
            {
            error = e;
            }
        Assertions.assertNotNull(error);
        }

    @Test
    void missingProjectResourceValueSource() throws MuseInstantiationException
        {
        MuseProject project = new SimpleProject(new InMemoryResourceStorage());
        ValueSourceConfiguration config = ValueSourceConfiguration.forType(ProjectResourceValueSource.TYPE_ID);
        config.setSource(ValueSourceConfiguration.forValue("nonexistent"));
        MuseValueSource source = config.createSource(project);
        Assertions.assertTrue(source instanceof ProjectResourceValueSource);

        MuseExecutionError error = null;
        try
            {
            source.resolveValue(new MockStepExecutionContext());
            }
        catch (MuseExecutionError e)
            {
            error = e;
            }
        Assertions.assertNotNull(error);
        }

    @Test
    void notTrue() throws MuseExecutionError
        {
        ValueSourceConfiguration source_value = ValueSourceConfiguration.forValue(true);
        MuseValueSource source = ValueSourceConfiguration.forTypeWithSource(NotValueSource.TYPE_ID, source_value).createSource();
        Assertions.assertEquals(false, source.resolveValue(new MockStepExecutionContext()));
        }

    @Test
    void notFalse() throws MuseExecutionError
        {
        ValueSourceConfiguration source_value = ValueSourceConfiguration.forValue(false);
        MuseValueSource source = ValueSourceConfiguration.forTypeWithSource(NotValueSource.TYPE_ID, source_value).createSource();
        Assertions.assertEquals(true, source.resolveValue(new MockStepExecutionContext()));
        }

    @Test
    void notNull() throws MuseInstantiationException
        {
        ValueSourceConfiguration source_value = ValueSourceConfiguration.forValue(null);
        MuseValueSource source = ValueSourceConfiguration.forTypeWithSource(NotValueSource.TYPE_ID, source_value).createSource();
        try
            {
            source.resolveValue(new MockStepExecutionContext());
            Assertions.fail("an exception should have been thrown");
            }
        catch (MuseExecutionError e)
            {
            // all good!
            }
        catch (Throwable e)
            {
            Assertions.fail("wrong exception was thrown");
            }
        }

    @Test
    void notString() throws MuseInstantiationException
        {
        ValueSourceConfiguration source_value = ValueSourceConfiguration.forValue("string");
        MuseValueSource source = ValueSourceConfiguration.forTypeWithSource(NotValueSource.TYPE_ID, source_value).createSource();
        try
            {
            source.resolveValue(new MockStepExecutionContext());
            Assertions.fail("an exception should have been thrown");
            }
        catch (MuseExecutionError e)
            {
            // all good!
            }
        catch (Throwable e)
            {
            Assertions.fail("wrong exception was thrown");
            }
        }

    @Test
    void formatNullDateParamAndFormatParams() throws MuseExecutionError
        {
        Object result = resolveDateFormatSource(null, null, null);
        long time = Long.parseLong(result.toString());
        Assertions.assertTrue(new TimeRange(System.currentTimeMillis(), 50).isInRange(time));
        }

    @Test
    void formatNullDateAndFormatParam() throws MuseExecutionError
        {
        Object result = resolveDateFormatSource(ValueSourceConfiguration.forType(NullValueSource.TYPE_ID), null, null);
        long time = Long.parseLong(result.toString());
        Assertions.assertTrue(new TimeRange(System.currentTimeMillis(), 50).isInRange(time));
        }

    @Test
    void formatNullDateParamAndFormat() throws MuseExecutionError
        {
        Object result = resolveDateFormatSource(null, ValueSourceConfiguration.forType(NullValueSource.TYPE_ID), null);
        long time = Long.parseLong(result.toString());
        Assertions.assertTrue(new TimeRange(System.currentTimeMillis(), 50).isInRange(time));
        }

    @Test
    void formatNow() throws MuseExecutionError
        {
        String expected = new SimpleDateFormat(NOW_DATE_FORMAT).format(new Date());
        Object result = resolveDateFormatSource(null, ValueSourceConfiguration.forValue(NOW_DATE_FORMAT), null);
        Assertions.assertEquals(expected, result);
        }

    @Test
    void formatDate() throws MuseExecutionError, ParseException
        {
        String expected = "05291998123456";
        Date parsed = new SimpleDateFormat(NOW_DATE_FORMAT).parse(expected);
        MockStepExecutionContext context = new MockStepExecutionContext();
        final String var_name = "date";
        context.setVariable(var_name, parsed);
        Object result = resolveDateFormatSource(ValueSourceConfiguration.forSource(VariableValueSource.TYPE_ID, ValueSourceConfiguration.forValue(var_name)), ValueSourceConfiguration.forValue(NOW_DATE_FORMAT), context);
        Assertions.assertEquals(expected, result);
        }

    private final static String NOW_DATE_FORMAT = "MMddyyyyHHmmss";

    private Object resolveDateFormatSource(ValueSourceConfiguration date_param, ValueSourceConfiguration format_param, StepExecutionContext context) throws MuseExecutionError
        {
        StepExecutionContext step_context = new MockStepExecutionContext();
        if (context != null)
            step_context = context;
        ValueSourceConfiguration config = ValueSourceConfiguration.forType(DateFormatValueSource.TYPE_ID);
        if (date_param != null)
            config.addSource(DateFormatValueSource.DATE_PARAM, date_param);
        if (format_param != null)
            config.addSource(DateFormatValueSource.FORMAT_PARAM, format_param);
        MuseValueSource source = config.createSource();
        return source.resolveValue(step_context);
        }

    @Test
    void toStringFormatting()
        {
        ValueSourceConfiguration config = ValueSourceConfiguration.forType(LogMessage.TYPE_ID);
        config.setValue("value");
        config.setSource(ValueSourceConfiguration.forValue("subsource"));
        config.addSource("nameA", ValueSourceConfiguration.forValue("valA"));
        config.addSource(0, ValueSourceConfiguration.forValue("first"));

        String stringified = config.toString();
        Assertions.assertTrue(stringified.contains("value"));
        Assertions.assertTrue(stringified.contains("subsource"));
        Assertions.assertTrue(stringified.contains("nameA"));
        Assertions.assertTrue(stringified.contains("valA"));
        Assertions.assertTrue(stringified.contains("first"));
        }

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
    void propertySource() throws MuseInstantiationException, ValueSourceResolutionError
        {
        ValueSourceConfiguration config = ValueSourceConfiguration.forType(PropertySource.TYPE_ID);
        config.addSource(PropertySource.NAME_PARAM, ValueSourceConfiguration.forValue("length"));
        config.addSource(PropertySource.TARGET_PARAM, ValueSourceConfiguration.forValue("12345"));
        SimpleProject project = new SimpleProject();

        // resolve the source
        MuseValueSource source = config.createSource(project);
        Object value = source.resolveValue(new ProjectExecutionContext(project));

        Assertions.assertEquals(5, value);
        }

    @Test
    void systemVariableSource() throws MuseInstantiationException, ValueSourceResolutionError
        {
        ValueSourceConfiguration config = ValueSourceConfiguration.forType(SystemVariableSource.TYPE_ID);
        config.setSource(ValueSourceConfiguration.forValue(MockSystemVariable.NAME));
        SimpleProject project = new SimpleProject();

        // resolve the source
        MuseValueSource source = config.createSource(project);
        Object value = source.resolveValue(new DefaultTestExecutionContext(project, new SteppedTest(new StepConfiguration(LogMessage.TYPE_ID))));

        Assertions.assertEquals(MockSystemVariable.VALUE, value);
        }

    @Test
    void randomNumber1to5() throws MuseInstantiationException, ValueSourceResolutionError
        {
        testRandom(1, 5, 50);
        }

    @Test
    void randomNumber0to11() throws MuseInstantiationException, ValueSourceResolutionError
        {
        testRandom(0, 11, 100);
        }

    private void testRandom(long min, long max, int repeats) throws MuseInstantiationException, ValueSourceResolutionError
        {
        ValueSourceConfiguration config = ValueSourceConfiguration.forType(RandomNumberValueSource.TYPE_ID);
        config.addSource(RandomNumberValueSource.MIN_PARAM, ValueSourceConfiguration.forValue(min));
        config.addSource(RandomNumberValueSource.MAX_PARAM, ValueSourceConfiguration.forValue(max));

        MuseProject project = new SimpleProject();
        MuseExecutionContext context = new MockStepExecutionContext(project);
        MuseValueSource source = config.createSource(project);
        for (int i = 0; i < repeats; i++)
            {
            Long result = (Long) source.resolveValue(context);
            Assertions.assertTrue(result < max + 1);
            Assertions.assertTrue(min - 1 < result);
            }
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
    void listSource() throws MuseInstantiationException, ValueSourceResolutionError
	    {
        ValueSourceConfiguration list = ValueSourceConfiguration.forType(ListSource.TYPE_ID);
        list.addSource(ValueSourceConfiguration.forValue(123));
        list.addSource(ValueSourceConfiguration.forValue(456));
        list.addSource(ValueSourceConfiguration.forValue(789));
        MuseValueSource source = list.createSource();
        List<Long> result = (List<Long>) source.resolveValue(new MockStepExecutionContext());

        Assertions.assertEquals(3, result.size());
        Assertions.assertEquals(123L, result.get(0).longValue());
        Assertions.assertEquals(456L, result.get(1).longValue());
        Assertions.assertEquals(789L, result.get(2).longValue());
        }
    }
