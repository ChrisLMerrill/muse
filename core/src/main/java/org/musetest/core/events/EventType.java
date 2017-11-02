package org.musetest.core.events;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public abstract class EventType
	{
	/**
	 * A unique identifier for this type.
	 */
	public abstract String getTypeId();

	/**
	 * A very short human-readable name for the type of event.
	 */
	public abstract String getName();
	}


