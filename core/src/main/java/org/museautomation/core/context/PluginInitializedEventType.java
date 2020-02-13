package org.museautomation.core.context;

import org.museautomation.core.*;
import org.museautomation.core.events.*;
import org.museautomation.core.plugins.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class PluginInitializedEventType extends EventType
	{
	@SuppressWarnings("WeakerAccess") // instantiated via reflection
	public PluginInitializedEventType()
		{
		super(TYPE_ID, "Plugin initialization");
		}

	@Override
	public String getDescription(MuseEvent event)
		{
		if (event.hasTag(MuseEvent.ERROR))
			return "ERROR: initialization failed for " + event.getAttribute(MuseEvent.DESCRIPTION) + " due to " + event.getAttribute(MuseEvent.ERROR_MESSAGE);
		else
			return "Initialized plugin: " + event.getAttribute(MuseEvent.DESCRIPTION);
		}

	public static MuseEvent create(MusePlugin plugin)
		{
		final MuseEvent event = new MuseEvent(TYPE_ID);
		String id = plugin.getId();
		String description;
		if (id == null)
			description = plugin.getClass().getSimpleName();
		else
			description = String.format("%s (%s)", plugin.getClass().getSimpleName(), id);
		event.setAttribute(MuseEvent.DESCRIPTION, description);
		return event;
		}

	public static MuseEvent create(MusePlugin plugin, String error_message)
		{
		MuseEvent event = create(plugin);
		event.addTag(MuseEvent.ERROR);
		event.setAttribute(MuseEvent.ERROR_MESSAGE, error_message);
		return event;
		}

	public final static String TYPE_ID = "plugin-initialized";
	}