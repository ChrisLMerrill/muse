package org.musetest.core.resource;

import org.musetest.core.resource.types.*;

import java.util.*;

/**
 * Describes attributes about a MuseResource. Used for querying the project
 * for resources matching those attributes.
 *
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class ResourceQueryParameters
    {
    public ResourceQueryParameters(ResourceType type)
        {
        _types.add(type);
        }

    public ResourceQueryParameters(String id)
        {
        _id = id;
        }

    public List<ResourceType> getTypes()
        {
        return _types;
        }

    public String getId()
        {
        return _id;
        }

    protected List<ResourceType> _types = new ArrayList<>();
    protected String _id = null;
    }


