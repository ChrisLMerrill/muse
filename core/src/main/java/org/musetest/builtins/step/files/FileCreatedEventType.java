package org.musetest.builtins.step.files;

import org.musetest.core.*;
import org.musetest.core.events.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class FileCreatedEventType extends EventType
	{
	private FileCreatedEventType()
		{
		super(TYPE_ID, "File Created");
		}

	public static MuseEvent create(String path, long size)
		{
		MuseEvent event = new MuseEvent(TYPE_ID);
		event.setAttribute(FILEPATH, path);
		event.setAttribute(SIZE, size);
		return event;
		}

    @Override
    public String getDescription(MuseEvent event)
        {
        //noinspection MalformedFormatString
        return String.format("File created: %s (%d bytes)", event.getAttribute(FILEPATH), event.getAttribute(SIZE));
        }

    private final static String FILEPATH = "path";
    private final static String SIZE = "bytes";
	public final static String TYPE_ID = "file-create";
	public final static EventType INSTANCE = new FileCreatedEventType();
	}