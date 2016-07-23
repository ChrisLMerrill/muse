package org.musetest.core.events;

import org.musetest.core.*;
import org.musetest.core.variables.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class SetVariableEvent extends MuseEvent
    {
    public SetVariableEvent(String name, Object value, VariableScope scope)
        {
        super(MuseEventType.SetVariable);
        _name = name;
        _value = value;
        _scope = scope;
        }

    @Override
    public String getDescription()
        {
        String value = (_value == null) ? "null" : _value.toString();
        return String.format("set %s variable $%s to: %s", _scope.name(), _name, value);
        }

    private String _name;
    private Object _value;
    private VariableScope _scope;
    }


