package org.musetest.core.tests.mocks;

import org.musetest.core.resource.*;
import org.musetest.core.resource.generic.*;
import org.musetest.core.resource.types.*;
import org.musetest.core.values.descriptor.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseSubsourceDescriptor(displayName = "Amount", description = "Amount to increment by (default is 1)", type = SubsourceDescriptor.Type.Named, name = "amount", optional = true)
public class MockMuseSubResource extends BaseMuseResource
    {
    @Override
    public ResourceType getType()
        {
        return new MockResourceSubType();
        }

    @SuppressWarnings("WeakerAccess")  // discovered and instantiated by reflection (see class ResourceTypes)
    public static class MockResourceSubType extends ResourceType
        {
        public MockResourceSubType()
            {
            super("mock-resource-subtype", "Mock SubResource", MockMuseSubResource.class);
            }

        @Override
        public ResourceDescriptor getDescriptor()
	        {
	        return new DefaultResourceDescriptor(this, SHORT_DESCRIPTION);
	        }
        }

    public final static String SHORT_DESCRIPTION = "A mock subsource resource type";
    }


