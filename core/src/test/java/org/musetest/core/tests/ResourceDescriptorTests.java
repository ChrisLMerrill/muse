package org.musetest.core.tests;

import org.junit.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Test;
import org.musetest.core.*;
import org.musetest.core.project.*;
import org.musetest.core.resource.generic.*;
import org.musetest.core.resource.types.*;
import org.musetest.core.tests.mocks.*;
import org.musetest.core.values.descriptor.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class ResourceDescriptorTests
	{
	@Test
	public void findResourceType()
	    {
	    final ResourceType type = _project.getResourceTypes().forResourceClass(MockMuseResource.class);
	    Assertions.assertEquals(type, new MockMuseResource.MockResourceType());
	    }

	@Test
	public void findResourceSubType()
	    {
	    final ResourceType type = _project.getResourceTypes().forResourceClass(MockMuseSubResource.class);
	    Assertions.assertEquals(type, new MockMuseSubResource.MockResourceSubType());
	    }

	@Test
	public void findImplementedDescriptor()
	    {
	    ResourceDescriptor descriptor = new MockMuseResource.MockResourceType().getDescriptor();
	    Assertions.assertEquals(MockMuseResource.SHORT_DESCRIPTION, descriptor.getShortDescription());
	    Assertions.assertEquals(1, descriptor.getSubsourceDescriptors().length);
	    SubsourceDescriptor source_descriptor = descriptor.getSubsourceDescriptors()[0];
	    Assertions.assertEquals(MockMuseResource.PARAM1_NAME, source_descriptor.getName());
	    Assertions.assertEquals(MockMuseResource.PARAM1_DISPLAY_NAME, source_descriptor.getDisplayName());
	    Assertions.assertEquals(MockMuseResource.PARAM1_DESCRIPTION, source_descriptor.getDescription());
	    }

	@Test
	public void findAnnotatedDescriptor()
	    {
	    ResourceDescriptor descriptor = new MockMuseSubResource.MockResourceSubType().getDescriptor();
	    Assertions.assertEquals(MockMuseSubResource.SHORT_DESCRIPTION, descriptor.getShortDescription());
	    Assertions.assertEquals(1, descriptor.getSubsourceDescriptors().length);
	    SubsourceDescriptor source_descriptor = descriptor.getSubsourceDescriptors()[0];
	    Assertions.assertEquals("amount", source_descriptor.getName());
	    Assertions.assertEquals("Amount", source_descriptor.getDisplayName());
	    }

	private MuseProject _project = new SimpleProject();
	}


