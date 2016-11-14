package org.musetest.core.resource;

import org.musetest.core.*;
import org.musetest.core.resource.types.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */

@SuppressWarnings("WeakerAccess") // used in UI tests
public class InMemoryResourceToken implements ResourceToken
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
    public List<String> getTags()
        {
        return _resource.getTags();
        }

    @Override
    public void setTags(List<String> tags)
        {
        throw new UnsupportedOperationException("Cannot modify the tags of a resource via a ResourceToken");
        }

    @Override
    public boolean addTag(String tag)
        {
        throw new UnsupportedOperationException("Cannot modify the tags of a resource via a ResourceToken");
        }

    @Override
    public boolean removeTag(String tag)
        {
        throw new UnsupportedOperationException("Cannot modify the tags of a resource via a ResourceToken");
        }

    @Override
    public boolean hasTag(String tag)
        {
        return _resource.hasTag(tag);
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


