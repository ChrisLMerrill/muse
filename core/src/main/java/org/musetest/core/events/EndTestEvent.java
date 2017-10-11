package org.musetest.core.events;

import org.musetest.core.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class EndTestEvent extends MuseEvent
    {
    public EndTestEvent(String description, boolean pass)
        {
        super(MuseEventType.EndTest);
		_description = description;
		_pass = pass;
        }

    @Override
    public String getDescription()
        {
        return _description;
        }

	@SuppressWarnings("unused")  // needed for JSON de/serialization
	private void setDescription(String description)
		{
		_description = description;
		}

	public boolean isPass()
		{
		return _pass;
		}

    private String _description;
    private boolean _pass;
    }


