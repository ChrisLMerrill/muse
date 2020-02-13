package org.museautomation.core.resource;

import org.museautomation.core.resource.types.*;

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

    private ResourceQueryParameters() {}

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

    @SuppressWarnings("unused")  // public API, used in UI
    public static ResourceQueryParameters forAllResources()
        {
        return new ResourceQueryParameters();
        }
    }


