package org.musetest.core.tests;

import org.junit.jupiter.api.*;
import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.project.*;
import org.musetest.core.step.*;
import org.musetest.core.step.descriptor.*;
import org.musetest.core.tests.mocks.unknownresource.*;
import org.musetest.core.values.*;
import org.musetest.core.values.descriptor.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
class StepDescriptorTests
    {
    @Test
    void unknownStepType()
        {
        final String type = "unknown_step_type";
        StepDescriptor descriptor = new StepDescriptors(project).get(type);
        Assertions.assertTrue(descriptor instanceof UnknownStepDescriptor);
        Assertions.assertEquals(type, descriptor.getType());
        Assertions.assertEquals(type, descriptor.getName());
        Assertions.assertNull(descriptor.getShortDescription());
        Assertions.assertEquals(UnknownStepDescriptor.ICON, descriptor.getIconDescriptor());
        Assertions.assertNull(descriptor.getInlineEditString());
        }

    @Test
    void locateDescriptorByImplementationClass()
        {
        StepDescriptor descriptor = project.getStepDescriptors().get(AnnotatedTestStep.class);
        Assertions.assertNotNull(descriptor);
        Assertions.assertTrue(descriptor instanceof AnnotatedStepDescriptor);
        }

    @Test
    void annotatedDescriptor()
        {
        StepDescriptor descriptor = project.getStepDescriptors().get(AnnotatedTestStep.class);
        Assertions.assertEquals(AnnotatedTestStep.class.getAnnotation(MuseTypeId.class).value(), descriptor.getType());
        Assertions.assertEquals(AnnotatedTestStep.class.getAnnotation(MuseStepName.class).value(), descriptor.getName());
        Assertions.assertEquals(AnnotatedTestStep.class.getAnnotation(MuseStepShortDescription.class).value(), descriptor.getShortDescription());
        Assertions.assertEquals(AnnotatedTestStep.class.getAnnotation(MuseStepIcon.class).value(), descriptor.getIconDescriptor());
        Assertions.assertEquals(AnnotatedTestStep.class.getAnnotation(MuseInlineEditString.class).value(), descriptor.getInlineEditString());
        }

    @Test
    void unannotatedCustomType()
        {
        StepDescriptor descriptor = project.getStepDescriptors().get(DummyStepType.class);
        Assertions.assertEquals(DummyStepType.class.getSimpleName(), descriptor.getType());
        Assertions.assertEquals(DummyStepType.class.getSimpleName(), descriptor.getName());
        Assertions.assertNull(descriptor.getShortDescription());
        Assertions.assertEquals(UnknownStepDescriptor.ICON, descriptor.getIconDescriptor());
        Assertions.assertNull(descriptor.getInlineEditString());
        }

    @Test
    void customDescriptorImplementationLookup()
        {
        StepDescriptor descriptor = project.getStepDescriptors().get(WithCustomDescriptor.class);
        Assertions.assertTrue(descriptor instanceof  TestStepDescriptor);
        Assertions.assertEquals(TestStepDescriptor.TYPE, descriptor.getType());
        Assertions.assertEquals(TestStepDescriptor.NAME, descriptor.getName());
        Assertions.assertEquals(TestStepDescriptor.SHORT_DESCRIPTION, descriptor.getShortDescription());
        Assertions.assertEquals(TestStepDescriptor.ICON, descriptor.getIconDescriptor());
        Assertions.assertEquals(TestStepDescriptor.TEST_STEP, descriptor.getInlineEditString());
        }

    @Test
    void unknownDescriptorShortDescriptionWithConfiguration()
        {
        StepDescriptor descriptor = project.getStepDescriptors().get("unknown1");
        testDescriptorAutoShortDescription(descriptor);
        }

    @Test
    void defaultDescriptorShortDescriptionWithConfiguration()
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
        Assertions.assertTrue(short_description.contains(source));
        Assertions.assertTrue(short_description.contains(value));
        }

    @Test
    void annotatedDescriptorShortDescriptionWithConfiguration()
        {
        StepDescriptor descriptor = project.getStepDescriptors().get(AnnotatedTestStep.class);
        StepConfiguration step_config = new StepConfiguration(AnnotatedTestStep.class.getAnnotation(MuseTypeId.class).value());
        final String value = "abc";
        step_config.addSource(AnnotatedTestStep.SOURCE_NAME, ValueSourceConfiguration.forValue(value));
        Assertions.assertTrue(descriptor.getShortDescription(step_config).contains(value));
        }

    @Test
    void customDescriptorShortDescriptionWithConfiguration()
        {
        StepDescriptor descriptor = project.getStepDescriptors().get(WithCustomDescriptor.class);
        StepConfiguration step_config = new StepConfiguration(TestStepDescriptor.TYPE);
        final String value = "xyz";
        step_config.addSource(TestStepDescriptor.SOURCE_NAME, ValueSourceConfiguration.forValue(value));
        Assertions.assertTrue(descriptor.getShortDescription(step_config).contains(value));
        }

    @Test
    void customDescriptionOverridesGenericShortDescription()
        {
        StepDescriptor descriptor = project.getStepDescriptors().get(AnnotatedTestStep.class);
        StepConfiguration step_config = new StepConfiguration("doesn't matter");
        final String description = "this step does something interesting";
        step_config.setMetadataField(StepConfiguration.META_DESCRIPTION, description);
        final String value = "xyz";
        step_config.addSource(TestStepDescriptor.SOURCE_NAME, ValueSourceConfiguration.forValue(value));
        Assertions.assertEquals(description, descriptor.getShortDescription(step_config));
        }

    @Test
    void stepSubsourceDescriptors()
        {
        StepDescriptor descriptor = project.getStepDescriptors().get(AnnotatedTestStep.class);
        SubsourceDescriptor[] descriptors = descriptor.getSubsourceDescriptors();
        Assertions.assertEquals(1, descriptors.length);

        SubsourceDescriptor param = descriptors[0];
        Assertions.assertEquals("param1", param.getDisplayName());
        Assertions.assertEquals("param1-description", param.getDescription());
        Assertions.assertEquals(SubsourceDescriptor.Type.Named, param.getType());
        Assertions.assertEquals("p1", param.getName());
        }

    private class DummyStepType implements MuseStep
        {
        public StepExecutionResult execute(StepExecutionContext context) throws MuseExecutionError
            { return null; }
        public StepConfiguration getConfiguration() { return null; }
        }

    @MuseTypeId("test-step-annotated")
    @MuseStepName("StepName")
    @MuseInlineEditString("inline edit {source1}")
    @MuseStepIcon("step icon")
    @MuseStepShortDescription("short description of the step")
    @MuseSubsourceDescriptor(displayName = "param1", description = "param1-description", type = SubsourceDescriptor.Type.Named, name = "p1")
    private class AnnotatedTestStep implements MuseStep
        {
        public StepExecutionResult execute(StepExecutionContext context) throws MuseExecutionError
            { return null; }
        public StepConfiguration getConfiguration() { return null; }
        final static String SOURCE_NAME = "source1";
        }

    @MuseStepDescriptorImplementation(TestStepDescriptor.class)
    private class WithCustomDescriptor implements MuseStep
        {
        public StepExecutionResult execute(StepExecutionContext context) throws MuseExecutionError
            { return null; }
        public StepConfiguration getConfiguration() { return null; }
        }

    private static MuseProject project = new SimpleProject();
    }


