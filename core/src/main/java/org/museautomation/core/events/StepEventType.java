package org.museautomation.core.events;

import org.museautomation.core.*;
import org.museautomation.core.step.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public abstract class StepEventType extends EventType
	{
	StepEventType(String typeid, String name)
		{
		super(typeid, name);
		}

	public static MuseEvent create(String typeid, StepConfiguration step)
		{
		MuseEvent event = new MuseEvent(typeid);
		final Object id = step.getMetadataField(StepConfiguration.META_ID);
		if (id != null && id instanceof Number)
			event.setAttribute(STEP_ID, ((Number) id).longValue());
		return event;
		}

	@SuppressWarnings("unused")  // public API
	public static Long getStepId(MuseEvent event)
		{
		Long id = 0L;
		Object value = event.getAttribute(STEP_ID);
		if (value instanceof Long)
			return (Long) value;
		if (value != null)
			{
			try
				{
				id = Long.parseLong(value.toString());
				}
			catch (NumberFormatException e)
				{
				// ignore
				}
			}
		return id;
		}

	@SuppressWarnings("WeakerAccess")  // public API
	public final static String STEP_ID = "stepid";
	@SuppressWarnings("WeakerAccess")  // public API
	public final static String STEP_DESCRIPTION = "stepdesc";

	public final static String INCOMPLETE = "incomplete";
	}