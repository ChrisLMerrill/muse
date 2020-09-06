package org.museautomation.builtins.plugins.resultstorage;

import org.museautomation.core.*;
import org.museautomation.core.events.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class SavedToLocalStorageEventType extends EventType
	{
	@SuppressWarnings("WeakerAccess") // instantiated by reflection
	public SavedToLocalStorageEventType()
		{
		super(TYPE_ID, "Task ");
		}

	@Override
	public String getDescription(MuseEvent event)
		{
		return String.format("Task data stored to %s", event.getAttribute(PATH));
		}

	public static MuseEvent create(String path)
		{
		MuseEvent event = new MuseEvent(TYPE_ID);
		event.setAttribute(PATH, path);
		return event;
		}

	public final static String TYPE_ID = "file-saved";
	public final static EventType INSTANCE = new SavedToLocalStorageEventType();

	public final static String PATH = "path";
	}


