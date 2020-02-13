package org.museautomation.builtins.value;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class NameValuePair
    {
    public NameValuePair(String name, Object value)
        {
        _name = name;
        _value = value;
        }

    public String getName()
        {
        return _name;
        }

    public void setName(String name)
        {
        _name = name;
        }

    public Object getValue()
        {
        return _value;
        }

    public void setValue(Object value)
        {
        _value = value;
        }

    @Override
    public boolean equals(Object obj)
        {
        if (!(obj instanceof NameValuePair))
            return false;
        NameValuePair other = (NameValuePair) obj;
        return Objects.equals(_name, other._name)
            && Objects.equals(_value, other._value);
        }

    @Override
    public String toString()
        {
        return String.format("(%s,%s)", _name, _value);
        }

    private String _name;
    private Object _value;
    }