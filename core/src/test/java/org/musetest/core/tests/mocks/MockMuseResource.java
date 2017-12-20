package org.musetest.core.tests.mocks;

import org.musetest.core.resource.*;
import org.musetest.core.resource.generic.*;
import org.musetest.core.resource.types.*;
import org.musetest.core.values.descriptor.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class MockMuseResource extends BaseMuseResource
    {
    @Override
    public ResourceType getType()
        {
        return new MockResourceType();
        }

    @SuppressWarnings("WeakerAccess")  // discovered and instantiated by reflection (see class ResourceTypes)
    public static class MockResourceType extends ResourceType
        {
        public MockResourceType()
            {
            super("mock-resource", "Mock Resource", MockMuseResource.class);
            }

        @Override
        public ResourceDescriptor getDescriptor()
	        {
            return _descriptor;
	        }

        private DefaultResourceDescriptor _descriptor = new DefaultResourceDescriptor(this)
	        {
	        @Override
	        public String getShortDescription()
		        {
		        return SHORT_DESCRIPTION;
		        }

	        @Override
	        public SubsourceDescriptor[] getSubsourceDescriptors()
		        {
		        return _subsources;
		        }

	        private SubsourceDescriptor[] _subsources =
		        {
	            new SimpleSubsourceDescriptor(PARAM1_DISPLAY_NAME, PARAM1_NAME, PARAM1_DESCRIPTION, 0, false, SubsourceDescriptor.Type.Named, null)
		        };
	        };
        }

    public final static String SHORT_DESCRIPTION = "A mock resource for testing";
    public final static String PARAM1_NAME = "param1";
    public final static String PARAM1_DISPLAY_NAME = "First Parameter";
    public final static String PARAM1_DESCRIPTION = "This parameter provides some input";
    }


