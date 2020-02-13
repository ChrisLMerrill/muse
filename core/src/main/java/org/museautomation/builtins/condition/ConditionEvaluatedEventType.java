package org.museautomation.builtins.condition;

import org.museautomation.core.*;
import org.museautomation.core.events.*;

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