package org.musetest.core.events;

import org.musetest.core.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class SetVariableEvent extends MuseEvent
    {
    public SetVariableEvent(String name, Object value)
        {
        super(MuseEventType.SetVariable);
        _name = name;
        _value = value;
        }

    @Override
    public String getDescription()
        {
        String value = (_value == null) ? "null" : _value.toString();
        return String.format("set variable $%s to: %s", _name, value);
        }

    private String _name;
    private Object _value;
    }


