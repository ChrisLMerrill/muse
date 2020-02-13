package org.museautomation.builtins.step.files;

import org.museautomation.core.*;
import org.museautomation.core.events.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class DownloadCompletedEventType extends EventType
	{
    @SuppressWarnings("WeakerAccess")  // instantiated by reflection in EventTypes
	public DownloadCompletedEventType()
		{
		super(TYPE_ID, "Download completed");
		}

	public static MuseEvent create(String url, long bytes_read)
		{
		MuseEvent event = new MuseEvent(TYPE_ID);
		event.setAttribute(URL, url);
		event.setAttribute(BYTES, bytes_read);
		return event;
		}

    @Override
    public String getDescription(MuseEvent event)
        {
        return String.format("Download completed (%d bytes): %s", (Long) event.getAttribute(BYTES), event.getAttribute(URL));
        }

    private final static String URL = "url";
    private final static String BYTES = "bytes";
	public final static String TYPE_ID = "download-complete";
	public final static EventType INSTANCE = new DownloadCompletedEventType();
	}