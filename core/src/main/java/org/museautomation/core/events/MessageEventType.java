package org.museautomation.core.events;

import org.museautomation.core.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class MessageEventType extends EventType
	{
	public MessageEventType()
		{
		super(TYPE_ID, "Message");
		}

	public static MuseEvent create(String message)
		{
		MuseEvent event = new MuseEvent(TYPE_ID);
		event.setAttribute(MuseEvent.DESCRIPTION, message);
		return event;
		}

	public final static String TYPE_ID = "message";
	public final static EventType INSTANCE = new MessageEventType();
	}