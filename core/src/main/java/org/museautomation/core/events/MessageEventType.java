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

	@SuppressWarnings("WeakerAccess") // public API
    public static MuseEvent createError(String message)
		{
		MuseEvent event = new MuseEvent(TYPE_ID);
		event.setAttribute(MuseEvent.DESCRIPTION, message);
		event.addTag(MuseEvent.ERROR);
		return event;
		}

	public static void raiseError(MuseExecutionContext context, String message)
		{
		context.raiseEvent(createError(message));
		}

	public static void raiseWarning(MuseExecutionContext context, String message)
		{
		MuseEvent event = new MuseEvent(TYPE_ID);
		event.setAttribute(MuseEvent.DESCRIPTION, message);
		event.addTag(MuseEvent.WARNING);
		context.raiseEvent(event);
		}

    public static void raiseMessageAndThrowError(MuseExecutionContext context, String message) throws MuseExecutionError
        {
        context.raiseEvent(MessageEventType.create(message));
        throw new MuseExecutionError("message");
        }

	public final static String TYPE_ID = "message";
	public final static EventType INSTANCE = new MessageEventType();
	}