package org.musetest.core.tests;

import org.junit.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Test;
import org.musetest.builtins.tests.mocks.*;
import org.musetest.core.*;
import org.musetest.core.project.*;
import org.musetest.core.values.descriptor.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class ValueSourceDescriptorTests
    {
    @Test
    public void testDescriptorImplementation()
        {
        // Test that we can lookup a descriptor and get the right implementation class
        SimpleProject project = new SimpleProject();
        ValueSourceDescriptor descriptor = project.getValueSourceDescriptors().get(TestValueSource.class.getAnnotation(MuseTypeId.class).value());
        Assertions.assertNotNull(descriptor);
        Assertions.assertEquals(TestValueSourceDescriptor.class, descriptor.getClass());
        }

    @Test
    public void testAnnotatedDescriptorImplementation()
        {
        // Test that we can lookup a descriptor and get the right implementation class
        SimpleProject project = new SimpleProject();
        ValueSourceDescriptor descriptor = project.getValueSourceDescriptors().get(TestAnnotatedValueSource.class.getAnnotation(MuseTypeId.class).value());
        Assertions.assertNotNull(descriptor);
        Assertions.assertEquals(AnnotatedValueSourceDescriptor.class, descriptor.getClass());

        Assertions.assertEquals(TestAnnotatedValueSource.class.getAnnotation(MuseTypeId.class).value(), descriptor.getType());
        Assertions.assertEquals(TestAnnotatedValueSource.class.getAnnotation(MuseValueSourceName.class).value(), descriptor.getName());
        Assertions.assertEquals(TestAnnotatedValueSource.class.getAnnotation(MuseValueSourceName.class).value(), descriptor.getName());
        Assertions.assertEquals(TestAnnotatedValueSource.class.getAnnotation(MuseValueSourceLongDescription.class).value(), descriptor.getLongDescription());
        Assertions.assertEquals(TestAnnotatedValueSource.class.getAnnotation(MuseValueSourceShortDescription.class).value(), descriptor.getShortDescription());
        Assertions.assertEquals(TestAnnotatedValueSource.class.getAnnotation(MuseValueSourceTypeGroup.class).value(), descriptor.getGroupName());

        // test an annotated sub-source descriptor
        SubsourceDescriptor[] sub_descriptors = descriptor.getSubsourceDescriptors();
        Assertions.assertEquals(2, sub_descriptors.length);

        // a named sub-source descriptor
        SubsourceDescriptor sub = sub_descriptors[0];
        Assertions.assertEquals("display-name1", sub.getDisplayName());
        Assertions.assertEquals("sub-description1", sub.getDescription());
        Assertions.assertEquals(SubsourceDescriptor.Type.Named, sub.getType());
        Assertions.assertEquals("name1", sub.getName());
        Assertions.assertEquals(-1, sub.getIndex());
        Assertions.assertEquals(false, sub.isOptional());
        Assertions.assertEquals(Object.class, sub.getResolutionType());

        // an optional indexed sub-source descriptor
        sub = sub_descriptors[1];
        Assertions.assertEquals("display-name2", sub.getDisplayName());
        Assertions.assertEquals("sub-description2", sub.getDescription());
        Assertions.assertEquals(SubsourceDescriptor.Type.Value, sub.getType());
        Assertions.assertEquals("", sub.getName());
        Assertions.assertEquals(true, sub.isOptional());
        Assertions.assertEquals(String.class, sub.getResolutionType());

        }
    }


