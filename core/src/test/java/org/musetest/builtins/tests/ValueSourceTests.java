package org.musetest.builtins.tests;

import org.junit.*;
import org.musetest.builtins.value.*;
import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.mocks.*;
import org.musetest.core.project.*;
import org.musetest.core.resource.*;
import org.musetest.core.steptest.*;
import org.musetest.core.values.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class ValueSourceTests
    {
    @Test
    public void stringConstantSource() throws StepExecutionError
        {
        ValueSourceConfiguration config = ValueSourceConfiguration.forValue("abc");
        MuseValueSource source = config.createSource();
        Assert.assertEquals("abc", source.resolveValue(null).toString());
        }

    @Test
    public void integerConstantSource() throws StepExecutionError
        {
        MuseValueSource source = ValueSourceConfiguration.forValue(456L).createSource();
        Object value = source.resolveValue(null);
        Assert.assertTrue(value instanceof Long);
        Assert.assertEquals(456L, value);
        }

    @Test
    public void booleanConstantSource() throws StepExecutionError
        {
        Assert.assertTrue((Boolean) ValueSourceConfiguration.forValue(true).createSource().resolveValue(null));
        Assert.assertFalse((Boolean) ValueSourceConfiguration.forValue(false).createSource().resolveValue(null));
        }

    @Test
    public void variableValueSource() throws StepExecutionError
        {
        MuseValueSource source = ValueSourceConfiguration.forSource(VariableValueSource.TYPE_ID, ValueSourceConfiguration.forValue("abc")).createSource();
        StepExecutionContext context = new DummyStepExecutionContext();
        context.setLocalVariable("abc", 123L);
        Assert.assertEquals(123L, source.resolveValue(context));
        }

    @Test
    public void variableValueSourceWithVariableName() throws StepExecutionError
        {
        ValueSourceConfiguration varname_holding_the_varname = ValueSourceConfiguration.forSource(VariableValueSource.TYPE_ID, ValueSourceConfiguration.forValue("varname"));
        MuseValueSource source = ValueSourceConfiguration.forSource(VariableValueSource.TYPE_ID, varname_holding_the_varname).createSource();
        StepExecutionContext context = new DummyStepExecutionContext();
        context.setLocalVariable("abc", 123L);
        context.setLocalVariable("varname", "abc");
        Assert.assertEquals(123L, source.resolveValue(context));
        }

    @Test
    public void stringConcatenation() throws StepExecutionError
        {
        ValueSourceConfiguration config = ValueSourceConfiguration.forType(AdditionSource.TYPE_ID);
        config.addSource(ValueSourceConfiguration.forValue("abc"));
        config.addSource(ValueSourceConfiguration.forValue("lmnop"));
        config.addSource(ValueSourceConfiguration.forValue("xyz"));
        MuseValueSource source = config.createSource(new SimpleProject());
        Assert.assertEquals("abclmnopxyz", source.resolveValue(new DummyStepExecutionContext()));
        }

    @Test
    public void mixedConcatenation() throws StepExecutionError
        {
        ValueSourceConfiguration config = ValueSourceConfiguration.forType(AdditionSource.TYPE_ID);
        config.addSource(ValueSourceConfiguration.forValue("abc"));
        config.addSource(ValueSourceConfiguration.forValue(8L));
        MuseValueSource source = config.createSource(new SimpleProject());
        Assert.assertEquals("abc8", source.resolveValue(new DummyStepExecutionContext()));
        }

    @Test
    public void additionOfNumbers() throws StepExecutionError
        {
        ValueSourceConfiguration config = ValueSourceConfiguration.forType(AdditionSource.TYPE_ID);
        config.addSource(ValueSourceConfiguration.forValue(1L));
        config.addSource(ValueSourceConfiguration.forValue(8L));
        MuseValueSource source = config.createSource(new SimpleProject());
        Assert.assertEquals(9L, source.resolveValue(new DummyStepExecutionContext()));
        }

    @Test
    public void projectResourceValueSource() throws StepExecutionError
        {
        final String test_id = "test_id";
        MuseTest test = new MockTest(MuseTestResultStatus.Success, test_id);
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

        StepExecutionError error = null;
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
    public void misconfiguredProjectResourceValueSource() throws StepConfigurationError
        {
        MuseProject project = new SimpleProject(new InMemoryResourceStore());
        ValueSourceConfiguration config = ValueSourceConfiguration.forType(ProjectResourceValueSource.TYPE_ID);
        config.setSource(ValueSourceConfiguration.forValue(null));
        MuseValueSource source = config.createSource(project);
        Assert.assertTrue(source instanceof ProjectResourceValueSource);

        DummyStepExecutionContext context = new DummyStepExecutionContext(project);
        StepExecutionError error = null;
        try
            {
            source.resolveValue(context);
            }
        catch (StepConfigurationError e)
            {
            error = e;
            }
        Assert.assertNotNull(error);
        }

    @Test
    public void missingProjectResourceValueSource() throws StepConfigurationError
        {
        MuseProject project = new SimpleProject(new InMemoryResourceStore());
        ValueSourceConfiguration config = ValueSourceConfiguration.forType(ProjectResourceValueSource.TYPE_ID);
        config.setSource(ValueSourceConfiguration.forValue("nonexistent"));
        MuseValueSource source = config.createSource(project);
        Assert.assertTrue(source instanceof ProjectResourceValueSource);

        DummyStepExecutionContext context = new DummyStepExecutionContext(project);
        StepExecutionError error = null;
        try
            {
            source.resolveValue(context);
            }
        catch (StepConfigurationError e)
            {
            error = e;
            }
        Assert.assertNotNull(error);
        }
    }
