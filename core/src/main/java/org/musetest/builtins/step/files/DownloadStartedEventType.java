package org.musetest.builtins.step.files;

import org.musetest.core.*;
import org.musetest.core.events.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class DownloadStartedEventType extends EventType
	{
    @SuppressWarnings("WeakerAccess")  // instantiated by reflection in EventTypes
	public DownloadStartedEventType()
		{
		super(TYPE_ID, "Download started");
		}

	public static MuseEvent create(String url)
		{
		MuseEvent event = new MuseEvent(TYPE_ID);
		event.setAttribute(URL, url);
		return event;
		}

    @Override
    public String getDescription(MuseEvent event)
        {
        return "Download started: " + event.getAttribute(URL);
        }

    private final static String URL = "url";
	public final static String TYPE_ID = "download-start";
	public final static EventType INSTANCE = new DownloadStartedEventType();
	}