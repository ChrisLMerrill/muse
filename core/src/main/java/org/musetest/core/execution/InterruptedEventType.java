package org.musetest.core.execution;

import org.musetest.core.events.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class InterruptedEventType extends EventType
	{
	@Override
	public String getTypeId()
		{
		return TYPE_ID;
		}

	@Override
	public String getName()
		{
		return "Interrupted";
		}

	public final static String TYPE_ID = "interrupted";
	public final static EventType TYPE = new InterruptedEventType();
	}


