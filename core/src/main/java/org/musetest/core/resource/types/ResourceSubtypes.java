package org.musetest.core.resource.types;

import org.musetest.core.resource.*;
import org.slf4j.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class ResourceSubtypes
    {
    public ResourceSubtypes(ClassLocator locator)
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
                    if (type instanceof ResourceSubtype)
                        {
                        ResourceSubtype subtype = (ResourceSubtype) type;
                        if (_subtypes.get(subtype.getTypeId()) != null)
                            LOG.warn("Duplicate ResourceSubtype found for id: " + subtype.getTypeId());
                        else
                            _subtypes.put(subtype.getTypeId().toLowerCase(), subtype);
                        }
                    }
                }
            catch (Exception e)
                {
                // no need to deal with abstract implementations, etc
                }
            }
        }

    public Collection<ResourceSubtype> getSubtypesOf(ResourceType type)
        {
        List<ResourceSubtype> subtypes = new ArrayList<>();
        for (ResourceSubtype subtype : _subtypes.values())
            if (subtype.isSubtypeOf(type))
                subtypes.add(subtype);
        return subtypes;
        }

    private Map<String, ResourceSubtype> _subtypes = new HashMap<>();

    private final static Logger LOG = LoggerFactory.getLogger(ResourceSubtypes.class);
    }


