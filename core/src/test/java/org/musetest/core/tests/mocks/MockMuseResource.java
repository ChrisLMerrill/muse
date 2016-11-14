package org.musetest.core.tests.mocks;

import org.musetest.core.resource.*;
import org.musetest.core.resource.types.*;

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
            super("mock", "Mock Resource", MockMuseResource.class);
            }
        }
    }


