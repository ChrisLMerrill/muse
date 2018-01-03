package org.musetest.core.resource.types;

import org.musetest.core.*;
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
                    if (type instanceof ResourceSubtype)
                        {
                        ResourceSubtype subtype = (ResourceSubtype) type;
                        if (_subtypes.get(subtype.getTypeId()) != null)
                            LOG.warn("Duplicate ResourceSubtype found for id: " + subtype.getTypeId());
                        else
	                        {
	                        _subtypes.put(subtype.getTypeId().toLowerCase(), subtype);
	                        if (!_primary_types.containsKey(subtype.getParentType().getTypeId()))
		                        _primary_types.put(subtype.getParentType().getTypeId(), subtype.getParentType());
	                        }
                        }
                    else
                        {
                        if (_primary_types.get(type.getTypeId()) != null)
                            LOG.warn("Duplicate ResourceType found for id: " + type.getTypeId());
                        else
                            _primary_types.put(type.getTypeId().toLowerCase(), type);
                        }
                    }
                }
            catch (Exception e)
                {
                // no need to deal with abstract implementations, etc
                }
            }
        }

    public Collection<ResourceType> getPrimary()
        {
        return _primary_types.values();
        }

    public List<ResourceSubtype> getSubtypesOf(ResourceType type)
        {
        List<ResourceSubtype> subtypes = new ArrayList<>();
        for (ResourceSubtype subtype : _subtypes.values())
            if (subtype.isSubtypeOf(type))
                subtypes.add(subtype);
        return subtypes;
        }

    public ResourceType forResourceClass(Class<? extends MuseResource> resource_class)
	    {
        for (ResourceType type : getPrimary())
            {
            if (resource_class.equals(type.getInterfaceClass()))
                return type;
            else
                {
                for (ResourceSubtype subtype : getSubtypesOf(type))
                    if (subtype.getInterfaceClass().equals(resource_class))
	                    return subtype;
                }
            }
	    return null;
	    }

    public ResourceType forIdIgnoreCase(String value)
        {
        return _primary_types.get(value.toLowerCase());
        }

    private Map<String, ResourceType> _primary_types = new HashMap<>();
    private Map<String, ResourceSubtype> _subtypes = new HashMap<>();

    private final static Logger LOG = LoggerFactory.getLogger(ResourceTypes.class);
    }


