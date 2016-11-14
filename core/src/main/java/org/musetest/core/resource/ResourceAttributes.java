package org.musetest.core.resource;

import org.musetest.core.resource.types.*;

import java.util.*;

/**
 * Describes attributes about a MuseResource. Used for querying the project
 * for resources matching those attributes.
 *
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class ResourceAttributes
    {
    public ResourceAttributes(ResourceType type)
        {
        _types.add(type);
        }

    public ResourceAttributes(String id)
        {
        _id = id;
        }

    List<ResourceType> _types = new ArrayList<>();
    String _id = null;
    }


