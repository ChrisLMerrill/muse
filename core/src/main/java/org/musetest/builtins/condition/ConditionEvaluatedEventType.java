package org.musetest.builtins.condition;

import org.musetest.core.events.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class ConditionEvaluatedEventType extends EventType
	{
	@Override
	public String getTypeId()
		{
		return TYPE_ID;
		}

	@Override
	public String getName()
		{
		return "Condition Evaluated";
		}

	public final static String TYPE_ID = "condition-evaluated";
	public final static EventType INSTANCE = new ConditionEvaluatedEventType();
	}


