package org.musetest.core.resource.types;

import org.musetest.core.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public abstract class ResourceType
    {
    protected ResourceType(String type_id, String name, Class interface_class)
        {
        _type_id = type_id;
        _name = name;
        _class = interface_class;
        }

    public Class getInterfaceClass()
        {
        return _class;
        }

    public String getName()
        {
        return _name;
        }

    public String getTypeId()
        {
        return _type_id;
        }

    public MuseResource create()
        {
        return null;
        }

    @Override
    public boolean equals(Object other)
        {
        return other instanceof  ResourceType && _type_id.equals(((ResourceType)other)._type_id);
        }

    private final String _type_id;
    private final String _name;
    private final Class _class;
    }


