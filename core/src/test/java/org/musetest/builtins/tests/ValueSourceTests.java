package org.musetest.builtins.tests;

import org.junit.*;
import org.musetest.builtins.condition.*;
import org.musetest.builtins.step.*;
import org.musetest.builtins.tests.mocks.*;
import org.musetest.builtins.value.*;
import org.musetest.builtins.value.property.*;
import org.musetest.builtins.value.sysvar.*;
import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.step.*;
import org.musetest.core.tests.mocks.*;
import org.musetest.core.project.*;
import org.musetest.core.resource.*;
import org.musetest.core.steptest.*;
import org.musetest.core.values.*;
import org.musetest.tests.utils.*;

import java.text.*;
import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class ValueSourceTests
    {
    @Test
    public void stringConstantSource() throws MuseExecutionError
        {
        ValueSourceConfiguration config = ValueSourceConfiguration.forValue("abc");
        MuseValueSource source = config.createSource();
        Assert.assertEquals("abc", source.resolveValue(null).toString());
        }

    @Test
    public void integerConstantSource() throws MuseExecutionError
        {
        MuseValueSource source = ValueSourceConfiguration.forValue(456L).createSource();
        Object value = source.resolveValue(null);
        Assert.assertTrue(value instanceof Long);
        Assert.assertEquals(456L, value);
        }

    @Test
    public void booleanConstantSource() throws MuseExecutionError
        {
        Assert.assertTrue((Boolean) ValueSourceConfiguration.forValue(true).createSource().resolveValue(null));
        Assert.assertFalse((Boolean) ValueSourceConfiguration.forValue(false).createSource().resolveValue(null));
        }

    @Test
    public void variableValueSource() throws MuseExecutionError
        {
        MuseValueSource source = ValueSourceConfiguration.forSource(VariableValueSource.TYPE_ID, ValueSourceConfiguration.forValue("abc")).createSource();
        StepExecutionContext context = new DummyStepExecutionContext();
        context.setVariable("abc", 123L);
        Assert.assertEquals(123L, source.resolveValue(context));
        }

    @Test
    public void variableValueSourceWithVariableName() throws MuseExecutionError
        {
        ValueSourceConfiguration varname_holding_the_varname = ValueSourceConfiguration.forSource(VariableValueSource.TYPE_ID, ValueSourceConfiguration.forValue("varname"));
        MuseValueSource source = ValueSourceConfiguration.forSource(VariableValueSource.TYPE_ID, varname_holding_the_varname).createSource();
        StepExecutionContext context = new DummyStepExecutionContext();
        context.setVariable("abc", 123L);
        context.setVariable("varname", "abc");
        Assert.assertEquals(123L, source.resolveValue(context));
        }

    @Test
    public void stringConcatenation() throws MuseExecutionError
        {
        ValueSourceConfiguration config = ValueSourceConfiguration.forType(AdditionSource.TYPE_ID);
        config.addSource(ValueSourceConfiguration.forValue("abc"));
        config.addSource(ValueSourceConfiguration.forValue("lmnop"));
        config.addSource(ValueSourceConfiguration.forValue("xyz"));
        MuseValueSource source = config.createSource(new SimpleProject());
        Assert.assertEquals("abclmnopxyz", source.resolveValue(new DummyStepExecutionContext()));
        }

    @Test
    public void mixedConcatenation() throws MuseExecutionError
        {
        ValueSourceConfiguration config = ValueSourceConfiguration.forType(AdditionSource.TYPE_ID);
        config.addSource(ValueSourceConfiguration.forValue("abc"));
        config.addSource(ValueSourceConfiguration.forValue(8L));
        MuseValueSource source = config.createSource(new SimpleProject());
        Assert.assertEquals("abc8", source.resolveValue(new DummyStepExecutionContext()));
        }

    @Test
    public void additionOfNumbers() throws MuseExecutionError
        {
        ValueSourceConfiguration config = ValueSourceConfiguration.forType(AdditionSource.TYPE_ID);
        config.addSource(ValueSourceConfiguration.forValue(1L));
        config.addSource(ValueSourceConfiguration.forValue(8L));
        MuseValueSource source = config.createSource(new SimpleProject());
        Assert.assertEquals(9L, source.resolveValue(new DummyStepExecutionContext()));
        }

    @Test
    public void projectResourceValueSource() throws MuseExecutionError
        {
        final String test_id = "test_id";
        MuseTest test = new MockTest(null, test_id);
        MuseProject project = new SimpleProject(new InMemoryResourceStore());
        project.addResource(test);

        ValueSourceConfiguration config = ValueSourceConfiguration.forType(ProjectResourceValueSource.TYPE_ID);
        config.setSource(ValueSourceConfiguration.forValue(test_id));
        MuseValueSource source = config.createSource(project);
        Assert.assertTrue(source instanceof ProjectResourceValueSource);

        DummyStepExecutionContext context = new DummyStepExecutionContext(project);
        Object resolved = source.resolveValue(context);
        Assert.assertEquals(test, resolved);
        }

    @Test
    public void missingSubsourceProjectResourceValueSource() throws StepConfigurationError
        {
        MuseProject project = new SimpleProject(new InMemoryResourceStore());
        ValueSourceConfiguration config = ValueSourceConfiguration.forType(ProjectResourceValueSource.TYPE_ID);

        MuseInstantiationException error = null;
        try
            {
            MuseValueSource source = config.createSource(project);
            Assert.assertTrue(source instanceof ProjectResourceValueSource);
            }
        catch (MuseInstantiationException e)
            {
            error = e;
            }
        Assert.assertNotNull(error);
        }

    @Test
    public void misconfiguredProjectResourceValueSource() throws MuseInstantiationException
        {
        MuseProject project = new SimpleProject(new InMemoryResourceStore());
        ValueSourceConfiguration config = ValueSourceConfiguration.forType(ProjectResourceValueSource.TYPE_ID);
        config.setSource(ValueSourceConfiguration.forValue(null));
        MuseValueSource source = config.createSource(project);
        Assert.assertTrue(source instanceof ProjectResourceValueSource);

        DummyStepExecutionContext context = new DummyStepExecutionContext(project);
        MuseExecutionError error = null;
        try
            {
            source.resolveValue(context);
            }
        catch (MuseExecutionError e)
            {
            error = e;
            }
        Assert.assertNotNull(error);
        }

    @Test
    public void missingProjectResourceValueSource() throws MuseInstantiationException
        {
        MuseProject project = new SimpleProject(new InMemoryResourceStore());
        ValueSourceConfiguration config = ValueSourceConfiguration.forType(ProjectResourceValueSource.TYPE_ID);
        config.setSource(ValueSourceConfiguration.forValue("nonexistent"));
        MuseValueSource source = config.createSource(project);
        Assert.assertTrue(source instanceof ProjectResourceValueSource);

        DummyStepExecutionContext context = new DummyStepExecutionContext(project);
        MuseExecutionError error = null;
        try
            {
            source.resolveValue(context);
            }
        catch (MuseExecutionError e)
            {
            error = e;
            }
        Assert.assertNotNull(error);
        }

    @Test
    public void notTrue() throws MuseExecutionError
        {
        StepExecutionContext context = new DummyStepExecutionContext();
        ValueSourceConfiguration source_value = ValueSourceConfiguration.forValue(true);
        MuseValueSource source = ValueSourceConfiguration.forTypeWithSource(NotValueSource.TYPE_ID, source_value).createSource();
        Assert.assertEquals(false, source.resolveValue(context));
        }

    @Test
    public void notFalse() throws MuseExecutionError
        {
        StepExecutionContext context = new DummyStepExecutionContext();
        ValueSourceConfiguration source_value = ValueSourceConfiguration.forValue(false);
        MuseValueSource source = ValueSourceConfiguration.forTypeWithSource(NotValueSource.TYPE_ID, source_value).createSource();
        Assert.assertEquals(true, source.resolveValue(context));
        }

    @Test
    public void notNull() throws MuseInstantiationException
        {
        StepExecutionContext context = new DummyStepExecutionContext();
        ValueSourceConfiguration source_value = ValueSourceConfiguration.forValue(null);
        MuseValueSource source = ValueSourceConfiguration.forTypeWithSource(NotValueSource.TYPE_ID, source_value).createSource();
        try
            {
            source.resolveValue(context);
            Assert.assertTrue("an exception should have been thrown", false);
            }
        catch (MuseExecutionError e)
            {
            // all good!
            }
        catch (Throwable e)
            {
            Assert.assertTrue("wrong exception was thrown", false);
            }
        }

    @Test
    public void notString() throws MuseInstantiationException
        {
        StepExecutionContext context = new DummyStepExecutionContext();
        ValueSourceConfiguration source_value = ValueSourceConfiguration.forValue("string");
        MuseValueSource source = ValueSourceConfiguration.forTypeWithSource(NotValueSource.TYPE_ID, source_value).createSource();
        try
            {
            source.resolveValue(context);
            Assert.assertTrue("an exception should have been thrown", false);
            }
        catch (MuseExecutionError e)
            {
            // all good!
            }
        catch (Throwable e)
            {
            Assert.assertTrue("wrong exception was thrown", false);
            }
        }

    @Test
    public void formatNullDateParamAndFormatParams() throws MuseExecutionError
        {
        Object result = resolveDateFormatSource(null, null, null);
        long time = Long.parseLong(result.toString());
        Assert.assertTrue(new TimeRange(System.currentTimeMillis(), 50).isInRange(time));
        }

    @Test
    public void formatNullDateAndFormatParam() throws MuseExecutionError
        {
        Object result = resolveDateFormatSource(ValueSourceConfiguration.forType(NullValueSource.TYPE_ID), null, null);
        long time = Long.parseLong(result.toString());
        Assert.assertTrue(new TimeRange(System.currentTimeMillis(), 50).isInRange(time));
        }

    @Test
    public void formatNullDateParamAndFormat() throws MuseExecutionError
        {
        Object result = resolveDateFormatSource(null, ValueSourceConfiguration.forType(NullValueSource.TYPE_ID), null);
        long time = Long.parseLong(result.toString());
        Assert.assertTrue(new TimeRange(System.currentTimeMillis(), 50).isInRange(time));
        }

    @Test public void formatNow() throws MuseExecutionError
        {
        String expected = new SimpleDateFormat(NOW_DATE_FORMAT).format(new Date());
        Object result = resolveDateFormatSource(null, ValueSourceConfiguration.forValue(NOW_DATE_FORMAT), null);
        Assert.assertEquals(expected, result);
        }

    @Test public void formatDate() throws MuseExecutionError, ParseException
        {
        String expected = "05291998123456";
        Date parsed = new SimpleDateFormat(NOW_DATE_FORMAT).parse(expected);
        DummyStepExecutionContext context = new DummyStepExecutionContext();
        final String var_name = "date";
        context.setVariable(var_name, parsed);
        Object result = resolveDateFormatSource(ValueSourceConfiguration.forSource(VariableValueSource.TYPE_ID, ValueSourceConfiguration.forValue(var_name)), ValueSourceConfiguration.forValue(NOW_DATE_FORMAT), context);
        Assert.assertEquals(expected, result);
        }

    @Test
    public void toStringFormatting()
        {
        ValueSourceConfiguration config = ValueSourceConfiguration.forType(LogMessage.TYPE_ID);
        config.setValue("value");
        config.setSource(ValueSourceConfiguration.forValue("subsource"));
        config.addSource("nameA", ValueSourceConfiguration.forValue("valA"));
        config.addSource(0, ValueSourceConfiguration.forValue("first"));

        String stringified = config.toString();
        Assert.assertTrue(stringified.contains("value"));
        Assert.assertTrue(stringified.contains("subsource"));
        Assert.assertTrue(stringified.contains("nameA"));
        Assert.assertTrue(stringified.contains("valA"));
        Assert.assertTrue(stringified.contains("first"));
        }

    @Test
    public void listContainsSource() throws MuseInstantiationException, ValueSourceResolutionError
        {
        ValueSourceConfiguration config = ValueSourceConfiguration.forType(ListContainsSource.TYPE_ID);
        List<String> list = new ArrayList<>();
        list.add("abc");
        list.add("def");
        config.addSource(ListContainsSource.LIST_PARAM, ValueSourceConfiguration.forTypeWithValue(MockValueSource.TYPE_ID, list));
        SimpleProject project = new SimpleProject();

        config.addSource(ListContainsSource.TARGET_PARAM, ValueSourceConfiguration.forValue("xyz"));
        MuseValueSource source = config.createSource(project);
        Assert.assertFalse((boolean) source.resolveValue(new DefaultTestExecutionContext(project, new SteppedTest(new StepConfiguration(LogMessage.TYPE_ID)))));

        config.addSource(ListContainsSource.TARGET_PARAM, ValueSourceConfiguration.forValue("abc"));
        source = config.createSource(project);
        Assert.assertTrue((boolean) source.resolveValue(new DefaultTestExecutionContext(project, new SteppedTest(new StepConfiguration(LogMessage.TYPE_ID)))));
        }

    @Test
    public void propertySource() throws MuseInstantiationException, ValueSourceResolutionError
        {
        ValueSourceConfiguration config = ValueSourceConfiguration.forType(PropertySource.TYPE_ID);
        config.addSource(PropertySource.NAME_PARAM, ValueSourceConfiguration.forValue("length"));
        config.addSource(PropertySource.TARGET_PARAM, ValueSourceConfiguration.forValue("12345"));
        SimpleProject project = new SimpleProject();

        // resolve the source
        MuseValueSource source = config.createSource(project);
        Object value = source.resolveValue(new DefaultTestExecutionContext(project, new SteppedTest(new StepConfiguration(LogMessage.TYPE_ID))));

        Assert.assertEquals(5, value);
        }

    @Test
    public void systemVariableSource() throws MuseInstantiationException, ValueSourceResolutionError
        {
        ValueSourceConfiguration config = ValueSourceConfiguration.forType(SystemVariableSource.TYPE_ID);
        config.setSource(ValueSourceConfiguration.forValue(MockSystemVariable.NAME));
        SimpleProject project = new SimpleProject();

        // resolve the source
        MuseValueSource source = config.createSource(project);
        Object value = source.resolveValue(new DefaultTestExecutionContext(project, new SteppedTest(new StepConfiguration(LogMessage.TYPE_ID))));

        Assert.assertEquals(MockSystemVariable.VALUE, value);
        }

    @Test
    public void environmentProviderVariableAccess() throws MuseInstantiationException, ValueSourceResolutionError
        {
        MuseProject project = new SimpleProject();
        MockEnvironmentProperties environment = new MockEnvironmentProperties();
        final String var_name = "env-var1";
        final String value = UUID.randomUUID().toString();
        environment.setVariable(var_name, value);
        EnvironmentSysvarProvider.overrideImplementation(project, environment);

        ValueSourceConfiguration env_config = ValueSourceConfiguration.forType(SystemVariableSource.TYPE_ID);
        env_config.setSource(ValueSourceConfiguration.forValue(EnvironmentSysvarProvider.VARNAME1));
        ValueSourceConfiguration vars_config = ValueSourceConfiguration.forType(PropertySource.TYPE_ID);
        vars_config.addSource(PropertySource.TARGET_PARAM, env_config);
        vars_config.addSource(PropertySource.NAME_PARAM, ValueSourceConfiguration.forValue(EnvironmentPropertiesProvider.VARS_NAME));
        ValueSourceConfiguration var_config = ValueSourceConfiguration.forType(PropertySource.TYPE_ID);
        var_config.addSource(PropertySource.TARGET_PARAM, vars_config);
        var_config.addSource(PropertySource.NAME_PARAM, ValueSourceConfiguration.forValue(var_name));

        // resolve the source
        MuseValueSource source = var_config.createSource(project);
        Object resolved = source.resolveValue(new DefaultTestExecutionContext(project, new SteppedTest(new StepConfiguration(LogMessage.TYPE_ID))));

        Assert.assertEquals(value, resolved);
        }

    @Test
    public void environmentProviderPropertyAccess() throws MuseInstantiationException, ValueSourceResolutionError
        {
        MuseProject project = new SimpleProject();
        MockEnvironmentProperties environment = new MockEnvironmentProperties();
        final String prop_name = "prop-var1";
        final String value = UUID.randomUUID().toString();
        environment.setProperty(prop_name, value);
        EnvironmentSysvarProvider.overrideImplementation(project, environment);

        ValueSourceConfiguration env_config = ValueSourceConfiguration.forType(SystemVariableSource.TYPE_ID);
        env_config.setSource(ValueSourceConfiguration.forValue(EnvironmentSysvarProvider.VARNAME1));
        ValueSourceConfiguration vars_config = ValueSourceConfiguration.forType(PropertySource.TYPE_ID);
        vars_config.addSource(PropertySource.TARGET_PARAM, env_config);
        vars_config.addSource(PropertySource.NAME_PARAM, ValueSourceConfiguration.forValue(EnvironmentPropertiesProvider.PROPS_NAME));
        ValueSourceConfiguration var_config = ValueSourceConfiguration.forType(PropertySource.TYPE_ID);
        var_config.addSource(PropertySource.TARGET_PARAM, vars_config);
        var_config.addSource(PropertySource.NAME_PARAM, ValueSourceConfiguration.forValue(prop_name));

        // resolve the source
        MuseValueSource source = var_config.createSource(project);
        Object resolved = source.resolveValue(new DefaultTestExecutionContext(project, new SteppedTest(new StepConfiguration(LogMessage.TYPE_ID))));

        Assert.assertEquals(value, resolved);
        }

    @Test
    public void environmentProviderUsernameAccess() throws MuseInstantiationException, ValueSourceResolutionError
        {
        MuseProject project = new SimpleProject();
        MockEnvironmentProperties environment = new MockEnvironmentProperties();
        final String value = UUID.randomUUID().toString();
        environment.setUsername(value);
        EnvironmentSysvarProvider.overrideImplementation(project, environment);

        ValueSourceConfiguration env_config = ValueSourceConfiguration.forType(SystemVariableSource.TYPE_ID);
        env_config.setSource(ValueSourceConfiguration.forValue(EnvironmentSysvarProvider.VARNAME1));
        ValueSourceConfiguration username_config = ValueSourceConfiguration.forType(PropertySource.TYPE_ID);
        username_config.addSource(PropertySource.TARGET_PARAM, env_config);
        username_config.addSource(PropertySource.NAME_PARAM, ValueSourceConfiguration.forValue(EnvironmentPropertiesProvider.USERNAME_NAME));

        // resolve the source
        MuseValueSource source = username_config.createSource(project);
        Object resolved = source.resolveValue(new DefaultTestExecutionContext(project, new SteppedTest(new StepConfiguration(LogMessage.TYPE_ID))));

        Assert.assertEquals(value, resolved);
        }

    private final static String NOW_DATE_FORMAT = "MMddyyyyHHmmss";

    private Object resolveDateFormatSource(ValueSourceConfiguration date_param, ValueSourceConfiguration format_param, StepExecutionContext context) throws MuseExecutionError
        {
        StepExecutionContext step_context = new DummyStepExecutionContext();
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
    }
