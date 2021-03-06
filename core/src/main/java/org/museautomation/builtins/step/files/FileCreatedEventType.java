package org.museautomation.builtins.step.files;

import org.museautomation.core.*;
import org.museautomation.core.events.*;

import java.io.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class FileCreatedEventType extends EventType
	{
    @SuppressWarnings("WeakerAccess")  // instantiated by reflection in EventTypes
	public FileCreatedEventType()
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

    public static File getFile(MuseEvent event)
        {
        return new File(event.getAttributeAsString(FILEPATH));
        }

    private final static String FILEPATH = "path";
    private final static String SIZE = "bytes";
	public final static String TYPE_ID = "file-create";
	public final static EventType INSTANCE = new FileCreatedEventType();
	}