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

    List<ResourceType> _types = new ArrayList<>();
    }


