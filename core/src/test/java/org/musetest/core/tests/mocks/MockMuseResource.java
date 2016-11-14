package org.musetest.core.tests.mocks;

import org.musetest.core.resource.*;
import org.musetest.core.resource.types.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class MockMuseResource extends BaseMuseResource
    {
    @Override
    public ResourceMetadata getMetadata()
        {
        if (_meta == null)
            _meta = new ResourceMetadata();
        return _meta;
        }

    @Override
    public ResourceType getType()
        {
        return new MockResourceType();
        }

    private ResourceMetadata _meta;

    @SuppressWarnings("WeakerAccess")  // discovered and instantiated by reflection (see class ResourceTypes)
    public static class MockResourceType extends ResourceType
        {
        public MockResourceType()
            {
            super("mock", "Mock Resource", MockMuseResource.class);
            }
        }
    }


