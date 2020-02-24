package org.museautomation.core.task;

import org.museautomation.core.values.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class TaskOutput
    {
    public String getName()
        {
        return _name;
        }

    public void setName(String name)
        {
        _name = name;
        }

    public ValueSourceConfiguration getSource()
        {
        return _source;
        }

    public void setSource(ValueSourceConfiguration source)
        {
        _source = source;
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

    public void setDefault(ValueSourceConfiguration value)
        {
        _default = value;
        }

    private String _name;
    private ValueSourceConfiguration _source;
    private boolean _required;
    private ValueSourceConfiguration _default;
    }