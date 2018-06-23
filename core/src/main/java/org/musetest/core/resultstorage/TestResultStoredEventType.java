package org.musetest.core.resultstorage;

import org.musetest.core.*;
import org.musetest.core.events.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class TestResultStoredEventType extends EventType
	{
	@SuppressWarnings("WeakerAccess") // instantiated by reflection
	public TestResultStoredEventType()
		{
		super(TYPE_ID, "Test Result Produced");
		}

	@Override
	public String getDescription(MuseEvent event)
		{
		return String.format("Stored %s in %s", event.getAttribute(RESULT_DESCRIPTION), event.getAttribute(VARIABLE_NAME));
		}

	public static MuseEvent create(String variable_name, String result_description)
		{
		MuseEvent event = new MuseEvent(TYPE_ID);
		event.setAttribute(VARIABLE_NAME, variable_name);
		event.setAttribute(RESULT_DESCRIPTION, result_description);
		return event;
		}

	public final static String TYPE_ID = "result-produced";
	public final static EventType INSTANCE = new TestResultStoredEventType();

	public final static String VARIABLE_NAME = "varname";
	public final static String RESULT_DESCRIPTION = "resultdesc";
	}


