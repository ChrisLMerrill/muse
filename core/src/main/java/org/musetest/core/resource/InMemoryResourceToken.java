package org.musetest.core.resource;

import org.musetest.core.*;

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
    public ResourceMetadata getMetadata()
        {
        return _resource.getMetadata();
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


