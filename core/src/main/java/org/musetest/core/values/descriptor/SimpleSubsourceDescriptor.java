package org.musetest.core.values.descriptor;

import com.fasterxml.jackson.annotation.*;
import org.musetest.core.values.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class SimpleSubsourceDescriptor implements SubsourceDescriptor
    {
    public SimpleSubsourceDescriptor(String display_name, String name, String description, int index, boolean optional, Type type, Class resolution_type)
        {
        _display_name = display_name;
        _name = name;
        _description = description;
        _index = index;
        _optional = optional;
        _type = type;
        _resolution_type = resolution_type;
        }

    public SimpleSubsourceDescriptor(MuseSubsourceDescriptor annotation)
        {
        _display_name = annotation.displayName();
        _name = annotation.name();
        _description = annotation.description();
        _index = annotation.index();
        _optional = annotation.optional();
        _type = annotation.type();
        _resolution_type = annotation.resolutionType();
        _default_value_string = annotation.defaultValue();
        }

    // for JSON deserialization
    private SimpleSubsourceDescriptor() { }

    @Override
    public String getDisplayName()
        {
        return _display_name;
        }

    public void setDisplayName(String display_name)
        {
        _display_name = display_name;
        }

    @Override
    public String getDescription()
        {
        return _description;
        }

    public void setDescription(String description)
        {
        _description = description;
        }

    @Override
    public String getName()
        {
        return _name;
        }

    public void setName(String name)
        {
        _name = name;
        }

    @Override
    public int getIndex()
        {
        return _index;
        }

    public void setIndex(int index)
        {
        _index = index;
        }

    @Override
    public boolean isOptional()
        {
        return _optional;
        }

    public void setOptional(boolean optional)
        {
        _optional = optional;
        }

    @Override
    public Type getType()
        {
        return _type;
        }

    void setType(Type type)
        {
        _type = type;
        }

    public String getDefaultValueString()
        {
        return _default_value_string;
        }

    public void setDefaultValueString(String default_value_string)
        {
        _default_value_string = default_value_string;
        }

    @Override
    @JsonIgnore
    public Class getResolutionType()
        {
        return _resolution_type;
        }

    @Override
    public String getOneLineSummary()
        {
        StringBuilder builder = new StringBuilder();
        switch (_type)
            {
            case Named:
                builder.append(String.format("%s (%s) - %s", _display_name, _name, _description));
                break;
            case Value:
                builder.append(String.format("%s - %s", _display_name, _description));
                break;
            }
        if (_optional)
            builder.append(" (optional)");
        return builder.toString();
        }

    @Override
    @JsonIgnore
    public ValueSourceConfiguration getDefault()
	    {
	    if (_default_value_string == null)
	        return null;
	    return ValueSourceConfiguration.parsePrimitive(_default_value_string);
	    }

    private String _display_name;
    private String _name;
    private String _description;
    private int _index;
    private boolean _optional;
    private Type _type;
    private Class _resolution_type;
    private String _default_value_string;
    }


