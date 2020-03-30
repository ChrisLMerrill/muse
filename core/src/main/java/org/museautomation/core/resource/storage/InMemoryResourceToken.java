package org.museautomation.core.resource.storage;

import org.museautomation.core.*;
import org.museautomation.core.metadata.*;
import org.museautomation.core.resource.*;
import org.museautomation.core.resource.types.*;
import org.museautomation.core.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */

public class InMemoryResourceToken implements ResourceToken<MuseResource>
    {
    public InMemoryResourceToken(MuseResource resource)
        {
        if (resource == null)
            throw new IllegalArgumentException("resource parameter is required");
        _resource = resource;
        }

    @Override
    public MuseResource getResource()
        {
        return _resource;
        }

    @Override
    public String getId()
        {
        return _resource.getId();
        }

    @Override
    public void setId(String id)
        {
        throw new UnsupportedOperationException("Cannot set the ID of a resource via a ResourceToken");
        }

    @Override
    public ResourceType getType()
        {
        return _resource.getType();
        }

    @Override
    public TagContainer tags()
        {
        return _resource.tags();
        }

    @Override
    public ContainsMetadata metadata()
        {
        return _resource.metadata();
        }

    @Override
    public boolean equals(Object obj)
        {
        if (!(obj instanceof InMemoryResourceToken))
            return false;
        InMemoryResourceToken other = (InMemoryResourceToken) obj;
        return _resource == other._resource;
        }

    private final MuseResource _resource;
    }