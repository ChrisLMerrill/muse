package org.musetest.core.resource.types;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public abstract class ResourceSubtype extends ResourceType
    {
    public ResourceSubtype(String type_id, String name, Class interface_class, ResourceType parent_type)
        {
        super(type_id, name, interface_class);
        _parent_type_id = parent_type.getTypeId();
        }

    public boolean isSubtypeOf(ResourceType type)
        {
        return _parent_type_id.equals(type.getTypeId());
        }

    public boolean isSubtype()
        {
        return true;
        }

    private String _parent_type_id;
    }


