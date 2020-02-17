package org.museautomation.core.events;

import org.museautomation.core.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class EndSuiteTaskEventType extends EventType
	{
	@SuppressWarnings("WeakerAccess") // instantiated via reflection
	public EndSuiteTaskEventType()
		{
		super(TYPE_ID, "End Task in Suite");
		}

	@Override
	public String getDescription(MuseEvent event)
		{
		return "Task Complete";
		}

	@SuppressWarnings("unused")  // for use by extensions
	public static String getConfigVariableName(MuseEvent event)
		{
		return event.getAttribute(StartSuiteTaskEventType.CONFIG_VAR_NAME).toString();
		}

	public static MuseEvent create(String config_var_name)
		{
		final MuseEvent event = new MuseEvent(TYPE_ID);
		event.setAttribute(StartSuiteTaskEventType.CONFIG_VAR_NAME, config_var_name);
		return event;
		}

	public final static String TYPE_ID = "end-suite-task";
	}