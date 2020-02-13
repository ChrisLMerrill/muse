package org.museautomation.core.tests;

import org.junit.jupiter.api.*;
import org.museautomation.core.*;
import org.museautomation.core.project.*;
import org.museautomation.core.resource.generic.*;
import org.museautomation.core.resource.types.*;
import org.museautomation.core.tests.mocks.*;
import org.museautomation.core.values.descriptor.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
class ResourceDescriptorTests
	{
	@Test
    void findResourceType()
	    {
	    final ResourceType type = _project.getResourceTypes().forResourceClass(MockMuseResource.class);
	    Assertions.assertEquals(type, new MockMuseResource.MockResourceType());
	    }

	@Test
    void findResourceSubType()
	    {
	    final ResourceType type = _project.getResourceTypes().forResourceClass(MockMuseSubResource.class);
	    Assertions.assertEquals(type, new MockMuseSubResource.MockResourceSubType());
	    }

	@Test
    void findImplementedDescriptor()
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
    void findAnnotatedDescriptor()
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


