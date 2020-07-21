package org.museautomation.builtins.plugins.resultstorage;

import org.museautomation.core.*;
import org.museautomation.core.events.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class LocalStorageLocationEventType extends EventType
    {
    // instantiated via reflection
   	public LocalStorageLocationEventType()
   		{
   		super(TYPE_ID, "Local storage location");
   		}

   	@Override
   	public String getDescription(MuseEvent event)
   		{
   		String path = event.getAttributeAsString(BASE_PATH);
        if (event.hasTag(MuseEvent.ERROR))
            return String.format("ERROR: Unable to set local storage location to %s due to %s", path, event.getAttribute(MuseEvent.ERROR_MESSAGE));
        else
            return "local storage location is set to " + path;
   		}

    @SuppressWarnings("unused")  // public API
    public String getBasePath(MuseEvent event)
        {
        return event.getAttribute(BASE_PATH).toString();
        }

    @Deprecated
    @SuppressWarnings("unused")  // public API
    public String getTaskPath(MuseEvent event)
        {
        return event.getAttribute(BASE_PATH).toString();
        }

   	public static MuseEvent create(String path, String error_message)
   		{
   		final MuseEvent event = new MuseEvent(TYPE_ID);
   		event.setAttribute(BASE_PATH, path);
   		if (error_message != null)
            {
            event.addTag(MuseEvent.ERROR);
            event.setAttribute(MuseEvent.ERROR, error_message);
            }
   		return event;
   		}

    public final static String TYPE_ID = "local-storage-location";
    private final static String BASE_PATH = "base-path";
    }