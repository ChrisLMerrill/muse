package org.musetest.core.resource.types;

import org.musetest.core.*;
import org.musetest.core.resource.generic.*;

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

    public boolean isSubtype()
        {
        return false;
        }

    public ResourceDescriptor getDescriptor()
	    {
	    if (_descriptor == null)
	    	_descriptor = new DefaultResourceDescriptor(this);
	    return _descriptor;
	    }

    public MuseResource create()
        {
        try
            {
            return (MuseResource) getInterfaceClass().newInstance();
            }
        catch (Exception e)
            {
            return null;
            }
        }

    @Override
    public boolean equals(Object other)
        {
        return other instanceof ResourceType && _type_id.equals(((ResourceType)other)._type_id);
        }

    @Override
    public String toString()
        {
        return _name;
        }

    private final String _type_id;
    private final String _name;
    private final Class _class;
    private ResourceDescriptor _descriptor = null;
    }


