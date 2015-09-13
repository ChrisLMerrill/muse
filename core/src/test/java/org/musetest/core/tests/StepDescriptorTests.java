package org.musetest.core.tests;

import org.junit.*;
import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.project.*;
import org.musetest.core.step.*;
import org.musetest.core.step.descriptor.*;
import org.musetest.core.steptest.*;
import org.musetest.core.tests.mocks.unknownresource.*;
import org.musetest.core.values.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class StepDescriptorTests
    {
    @Test
    public void unknownStepType()
        {
        final String type = "unknown_step_type";
        StepDescriptor descriptor = new StepDescriptors(project).get(type);
        Assert.assertTrue(descriptor instanceof UnknownStepDescriptor);
        Assert.assertEquals(type, descriptor.getType());
        Assert.assertEquals(type, descriptor.getName());
        Assert.assertEquals(null, descriptor.getShortDescription());
        Assert.assertEquals(UnknownStepDescriptor.ICON, descriptor.getIconDescriptor());
        Assert.assertNull(descriptor.getInlineEditString());
        }

    @Test
    public void locateDescriptorByImplementationClass()
        {
        StepDescriptor descriptor = project.getStepDescriptors().get(AnnotatedStepType.class);
        Assert.assertNotNull(descriptor);
        Assert.assertTrue(descriptor instanceof AnnotatedStepDescriptor);
        }

    @Test
    public void annotatedDescriptor()
        {
        StepDescriptor descriptor = project.getStepDescriptors().get(AnnotatedStepType.class);
        Assert.assertEquals(AnnotatedStepType.class.getAnnotation(MuseTypeId.class).value(), descriptor.getType());
        Assert.assertEquals(AnnotatedStepType.class.getAnnotation(MuseStepName.class).value(), descriptor.getName());
        Assert.assertEquals(AnnotatedStepType.class.getAnnotation(MuseStepShortDescription.class).value(), descriptor.getShortDescription());
        Assert.assertEquals(AnnotatedStepType.class.getAnnotation(MuseStepIcon.class).value(), descriptor.getIconDescriptor());
        Assert.assertEquals(AnnotatedStepType.class.getAnnotation(MuseInlineEditString.class).value(), descriptor.getInlineEditString());
        }

    @Test
    public void unannotatedCustomType()
        {
        StepDescriptor descriptor = project.getStepDescriptors().get(DummyStepType.class);
        Assert.assertEquals(DummyStepType.class.getSimpleName(), descriptor.getType());
        Assert.assertEquals(DummyStepType.class.getSimpleName(), descriptor.getName());
        Assert.assertEquals(null, descriptor.getShortDescription());
        Assert.assertEquals(UnknownStepDescriptor.ICON, descriptor.getIconDescriptor());
        Assert.assertNull(descriptor.getInlineEditString());
        }

    @Test
    public void customDescriptorImplementationLookup()
        {
        StepDescriptor descriptor = project.getStepDescriptors().get(WithCustomDescriptor.class);
        Assert.assertTrue(descriptor instanceof  TestStepDescriptor);
        Assert.assertEquals(TestStepDescriptor.TYPE, descriptor.getType());
        Assert.assertEquals(TestStepDescriptor.NAME, descriptor.getName());
        Assert.assertEquals(TestStepDescriptor.SHORT_DESCRIPTION, descriptor.getShortDescription());
        Assert.assertEquals(TestStepDescriptor.ICON, descriptor.getIconDescriptor());
        Assert.assertEquals(TestStepDescriptor.TEST_STEP, descriptor.getInlineEditString());
        }

    @Test
    public void unknownDescriptorShortDescriptionWithConfiguration()
        {
        StepDescriptor descriptor = project.getStepDescriptors().get("unknown1");
        testDescriptorAutoShortDescription(descriptor);
        }

    @Test
    public void defaultDescriptorShortDescriptionWithConfiguration()
        {
        StepDescriptor descriptor = project.getStepDescriptors().get(DummyStepType.class);
        testDescriptorAutoShortDescription(descriptor);
        }

    private void testDescriptorAutoShortDescription(StepDescriptor descriptor)
        {
        StepConfiguration step_config = new StepConfiguration("unknown1");
        final String source = "source_name";
        final String value = "value1";
        step_config.addSource(source, ValueSourceConfiguration.forValue(value));
        String short_description = descriptor.getShortDescription(step_config);
        Assert.assertTrue(short_description.contains(source));
        Assert.assertTrue(short_description.contains(value));
        }

    @Test
    public void annotatedDescriptorShortDescriptionWithConfiguration()
        {
        StepDescriptor descriptor = project.getStepDescriptors().get(AnnotatedStepType.class);
        StepConfiguration step_config = new StepConfiguration(AnnotatedStepType.class.getAnnotation(MuseTypeId.class).value());
        final String value = "abc";
        step_config.addSource(AnnotatedStepType.SOURCE_NAME, ValueSourceConfiguration.forValue(value));
        Assert.assertTrue(descriptor.getShortDescription(step_config).contains(value));
        }

    @Test
    public void customDescriptorShortDescriptionWithConfiguration()
        {
        StepDescriptor descriptor = project.getStepDescriptors().get(WithCustomDescriptor.class);
        StepConfiguration step_config = new StepConfiguration(TestStepDescriptor.TYPE);
        final String value = "xyz";
        step_config.addSource(TestStepDescriptor.SOURCE_NAME, ValueSourceConfiguration.forValue(value));
        Assert.assertTrue(descriptor.getShortDescription(step_config).contains(value));
        }

    @Test
    public void customDescriptionOverridesGenericShortDescription()
        {
        StepDescriptor descriptor = project.getStepDescriptors().get(AnnotatedStepType.class);
        StepConfiguration step_config = new StepConfiguration("doesn't matter");
        final String description = "this step does something interesting";
        step_config.setMetadataField(StepConfiguration.META_DESCRIPTION, description);
        final String value = "xyz";
        step_config.addSource(TestStepDescriptor.SOURCE_NAME, ValueSourceConfiguration.forValue(value));
        Assert.assertEquals(description, descriptor.getShortDescription(step_config));
        }

    private class DummyStepType implements MuseStep
        {
        public StepExecutionResult execute(StepExecutionContext context) throws StepExecutionError { return null; }
        public StepConfiguration getConfiguration() { return null; }
        }

    @MuseTypeId("test-step-annotated")
    @MuseStepName("StepName")
    @MuseInlineEditString("inline edit {source1}")
    @MuseStepIcon("step icon")
    @MuseStepShortDescription("short description of the step")
    private class AnnotatedStepType implements MuseStep
        {
        public StepExecutionResult execute(StepExecutionContext context) throws StepExecutionError { return null; }
        public StepConfiguration getConfiguration() { return null; }
        public final static String SOURCE_NAME = "source1";
        }

    @MuseStepDescriptorImplementation(TestStepDescriptor.class)
    private class WithCustomDescriptor implements MuseStep
        {
        public StepExecutionResult execute(StepExecutionContext context) throws StepExecutionError { return null; }
        public StepConfiguration getConfiguration() { return null; }
        }

    private static MuseProject project = new SimpleProject();
    }


