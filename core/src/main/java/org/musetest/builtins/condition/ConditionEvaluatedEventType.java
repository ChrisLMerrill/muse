package org.musetest.builtins.condition;

import org.musetest.core.*;
import org.musetest.core.events.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class ConditionEvaluatedEventType extends EventType
	{
	@SuppressWarnings("WeakerAccess")  // instantiated by reflection
	public ConditionEvaluatedEventType()
		{
		super(TYPE_ID, "Condition Evaluated");
		}

	public static MuseEvent create(String description)
		{
		MuseEvent event = new MuseEvent(TYPE_ID);
		event.setAttribute(MuseEvent.DESCRIPTION, description);
		return event;
		}

	public final static String TYPE_ID = "condition-evaluated";
	public final static EventType INSTANCE = new ConditionEvaluatedEventType();
	}