package org.musetest.core.events;

import org.musetest.core.*;
import org.musetest.core.context.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class SetVariableEventType extends EventType
	{
	@SuppressWarnings("WeakerAccess") // instantiated via reflection
	public SetVariableEventType()
		{
		super(TYPE_ID, "Set Variable");
		}

	public static MuseEvent create(String name, Object value, ContextVariableScope scope)
		{
		MuseEvent event = new MuseEvent(TYPE_ID);
		event.setAttribute(NAME, name);
		event.setAttribute(VALUE, value == null ? null : value.toString());
		event.setAttribute(SCOPE, scope.name());
		return event;
		}

	@Override
	public String getDescription(MuseEvent event)
		{
		Object value = event.getAttribute(VALUE);
		String value_str = (value == null) ? "null" : value.toString();
		return String.format("set %s variable $%s to: %s", event.getAttribute(SCOPE), event.getAttribute(NAME), value_str);
		}

	public final static String TYPE_ID = "set-variable";
	public final static EventType INSTANCE = new SetVariableEventType();

	public final static String NAME = "name";
	@SuppressWarnings("WeakerAccess") // public API
	public final static String VALUE = "valuestr";
	@SuppressWarnings("WeakerAccess") // public API
	public final static String SCOPE = "scope";
	}