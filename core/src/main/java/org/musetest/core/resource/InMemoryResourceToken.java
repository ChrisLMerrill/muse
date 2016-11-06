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
        return _resource.getMetadata().getId().equals(other.getMetadata().getId());
        }

    private MuseResource _resource;
    }


