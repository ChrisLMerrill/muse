package org.musetest.builtins.tests;

import org.junit.*;
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
        Assert.assertNotNull(descriptor);
        Assert.assertEquals(TestValueSourceDescriptor.class, descriptor.getClass());
        }

    @Test
    public void testAnnotatedDescriptorImplementation()
        {
        // Test that we can lookup a descriptor and get the right implementation class
        SimpleProject project = new SimpleProject();
        ValueSourceDescriptor descriptor = project.getValueSourceDescriptors().get(TestAnnotatedValueSource.class.getAnnotation(MuseTypeId.class).value());
        Assert.assertNotNull(descriptor);
        Assert.assertEquals(AnnotatedValueSourceDescriptor.class, descriptor.getClass());

        Assert.assertEquals(TestAnnotatedValueSource.class.getAnnotation(MuseTypeId.class).value(), descriptor.getType());
        Assert.assertEquals(TestAnnotatedValueSource.class.getAnnotation(MuseValueSourceName.class).value(), descriptor.getName());
        Assert.assertEquals(TestAnnotatedValueSource.class.getAnnotation(MuseValueSourceName.class).value(), descriptor.getName());
        Assert.assertEquals(TestAnnotatedValueSource.class.getAnnotation(MuseValueSourceLongDescription.class).value(), descriptor.getLongDescription());
        Assert.assertEquals(TestAnnotatedValueSource.class.getAnnotation(MuseValueSourceShortDescription.class).value(), descriptor.getShortDescription());
        Assert.assertEquals(TestAnnotatedValueSource.class.getAnnotation(MuseValueSourceTypeGroup.class).value(), descriptor.getGroupName());

        // test an annotated sub-source descriptor
        SubsourceDescriptor[] sub_descriptors = descriptor.getSubsourceDescriptors();
        Assert.assertEquals(2, sub_descriptors.length);

        // a named sub-source descriptor
        SubsourceDescriptor sub = sub_descriptors[0];
        Assert.assertEquals("display-name1", sub.getDisplayName());
        Assert.assertEquals("sub-description1", sub.getDescription());
        Assert.assertEquals(SubsourceDescriptor.Type.Named, sub.getType());
        Assert.assertEquals("name1", sub.getName());
        Assert.assertEquals(-1, sub.getIndex());
        Assert.assertEquals(false, sub.isOptional());
        Assert.assertEquals(Object.class, sub.getResolutionType());

        // an optional indexed sub-source descriptor
        sub = sub_descriptors[1];
        Assert.assertEquals("display-name2", sub.getDisplayName());
        Assert.assertEquals("sub-description2", sub.getDescription());
        Assert.assertEquals(SubsourceDescriptor.Type.Indexed, sub.getType());
        Assert.assertEquals("", sub.getName());
        Assert.assertEquals(2, sub.getIndex());
        Assert.assertEquals(true, sub.isOptional());
        Assert.assertEquals(String.class, sub.getResolutionType());

        }
    }


