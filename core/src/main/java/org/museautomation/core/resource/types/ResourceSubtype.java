package org.museautomation.core.resource.types;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public abstract class ResourceSubtype extends ResourceType
    {
    public ResourceSubtype(String type_id, String name, Class interface_class, ResourceType parent_type)
        {
        super(type_id, name, interface_class);
        _parent_type = parent_type;
        }

    @SuppressWarnings("WeakerAccess") // public API
    public boolean isSubtypeOf(ResourceType type)
        {
        return _parent_type.getTypeId().equals(type.getTypeId());
        }

    public boolean isSubtype()
        {
        return true;
        }

    public ResourceType getParentType()
        {
        return _parent_type;
        }

    @Override
    public boolean equals(Object other)
        {
        return super.equals(other) && (other instanceof ResourceSubtype) && ((ResourceSubtype)other)._parent_type.equals(_parent_type);
        }

    private ResourceType _parent_type;
    }


