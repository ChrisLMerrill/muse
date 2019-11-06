package org.musetest.core.resultstorage;

import org.musetest.core.*;
import org.musetest.core.events.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class LocalStorageLocationEventType extends EventType
    {
    @SuppressWarnings("WeakerAccess") // instantiated via reflection
   	public LocalStorageLocationEventType()
   		{
   		super(TYPE_ID, "Local storage location");
   		}

   	@Override
   	public String getDescription(MuseEvent event)
   		{
        if (event.hasTag(MuseEvent.ERROR))
            return "ERROR: Unable to set local storage location to " + event.getAttribute(PATH) + " due to " + event.getAttribute(MuseEvent.ERROR_MESSAGE);
        else
            return "local storage location is set to " + event.getAttribute(PATH);
   		}

   	public static MuseEvent create(String path, String error_message)
   		{
   		final MuseEvent event = new MuseEvent(TYPE_ID);
   		event.setAttribute(PATH, path);
   		if (error_message != null)
            {
            event.addTag(MuseEvent.ERROR);
            event.setAttribute(MuseEvent.ERROR, error_message);
            }
   		return event;
   		}

    public final static String TYPE_ID = "local-storage-location";
    public final static String PATH = "path";
    }