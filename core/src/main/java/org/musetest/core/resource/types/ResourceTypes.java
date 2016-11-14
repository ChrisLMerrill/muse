package org.musetest.core.resource.types;

import org.musetest.core.resource.*;
import org.slf4j.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class ResourceTypes
    {
    public ResourceTypes(ClassLocator locator)
        {
        List<Class> implementors = locator.getImplementors(ResourceType.class);
        for (Class the_class : implementors)
            {
            try
                {
                Object obj = the_class.newInstance();
                if (obj instanceof ResourceType)
                    {
                    ResourceType type = (ResourceType) obj;
                    if (_types.get(type.getTypeId()) != null)
                        LOG.warn("Duplicate ResourceTypes found for id: " + type.getTypeId());
                    else
                        _types.put(type.getTypeId().toLowerCase(), type);
                    }
                }
            catch (Exception e)
                {
                // no need to deal with abstract implementations, etc
                }
            }
        }

    public Collection<ResourceType> getAll()
        {
        return _types.values();
        }

    public ResourceType forIdIgnoreCase(String value)
        {
        return _types.get(value.toLowerCase());
        }

    private Map<String, ResourceType> _types = new HashMap<>();

    private final static Logger LOG = LoggerFactory.getLogger(ResourceTypes.class);
    }


