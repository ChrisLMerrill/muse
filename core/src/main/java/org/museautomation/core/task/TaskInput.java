package org.museautomation.core.task;

import org.museautomation.*;
import org.museautomation.core.values.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class TaskInput
    {
    public String getName()
        {
        return _name;
        }

    public void setName(String name)
        {
        _name = name;
        }

    public MuseValueType getType()
        {
        return _type;
        }

    public void setType(MuseValueType type)
        {
        _type = type;
        }

    public boolean isRequired()
        {
        return _required;
        }

    public void setRequired(boolean required)
        {
        _required = required;
        }

    public ValueSourceConfiguration getDefault()
        {
        return _default;
        }

    public void setDefault(ValueSourceConfiguration default_value)
        {
        _default = default_value;
        }

    private String _name;
    private MuseValueType _type;
    private boolean _required;
    private ValueSourceConfiguration _default;
    }