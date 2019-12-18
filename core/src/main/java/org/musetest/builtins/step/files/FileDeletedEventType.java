package org.musetest.builtins.step.files;

import org.musetest.core.*;
import org.musetest.core.events.*;

import java.io.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class FileDeletedEventType extends EventType
	{
    @SuppressWarnings("WeakerAccess")  // instantiated by reflection in EventTypes
	public FileDeletedEventType()
		{
		super(TYPE_ID, "File Deleted");
		}

	public static MuseEvent create(String path)
		{
		MuseEvent event = new MuseEvent(TYPE_ID);
		event.setAttribute(FILEPATH, path);
		return event;
		}

    @Override
    public String getDescription(MuseEvent event)
        {
        return "File deleted: " + event.getAttribute(FILEPATH);
        }

    public static File getFile(MuseEvent event)
        {
        return new File(event.getAttributeAsString(FILEPATH));
        }

    private final static String FILEPATH = "path";
	public final static String TYPE_ID = "file-delete";
	public final static EventType INSTANCE = new FileDeletedEventType();
	}