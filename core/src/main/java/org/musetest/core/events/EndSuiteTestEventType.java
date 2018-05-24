package org.musetest.core.events;

import org.musetest.core.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class EndSuiteTestEventType extends EventType
	{
	@SuppressWarnings("WeakerAccess") // instantiated via reflection
	public EndSuiteTestEventType()
		{
		super(TYPE_ID, "End Test in Suite");
		}

	@Override
	public String getDescription(MuseEvent event)
		{
		return "Test Complete";
		}

	@SuppressWarnings("unused")  // for use by extensions
	public static String getConfigVariableName(MuseEvent event)
		{
		return event.getAttribute(StartSuiteTestEventType.CONFIG_VAR_NAME).toString();
		}

	public static MuseEvent create(String config_var_name)
		{
		final MuseEvent event = new MuseEvent(TYPE_ID);
		event.setAttribute(StartSuiteTestEventType.CONFIG_VAR_NAME, config_var_name);
		return event;
		}

	public final static String TYPE_ID = "end-suite-test";
	}