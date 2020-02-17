package org.museautomation.core.events;

import org.museautomation.core.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class StartSuiteTaskEventType extends EventType
	{
	@SuppressWarnings("WeakerAccess") // instantiated via reflection
	public StartSuiteTaskEventType()
		{
		super(TYPE_ID, "Start Task in Suite");
		}

	@Override
	public String getDescription(MuseEvent event)
		{
		return "Task Starting";
		}

	public static String getConfigVariableName(MuseEvent event)
		{
		return event.getAttribute(CONFIG_VAR_NAME).toString();
		}

	public static MuseEvent create(String config_var_name)
		{
		final MuseEvent event = new MuseEvent(TYPE_ID);
		event.setAttribute(CONFIG_VAR_NAME, config_var_name);
		return event;
		}

	public final static String TYPE_ID = "start-suite-task";

	public final static String CONFIG_VAR_NAME = "config";
	}