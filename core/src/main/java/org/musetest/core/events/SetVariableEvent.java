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
        super(SetVariableEventType.INSTANCE);
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

    public final static class SetVariableEventType extends EventType
	    {
	    @Override
	    public String getTypeId()
		    {
		    return TYPE_ID;
		    }

	    @Override
	    public String getName()
		    {
		    return "Set Variable";
		    }

	    public final static String TYPE_ID = "set-variable";
	    public final static EventType INSTANCE = new SetVariableEventType();
	    }
    }