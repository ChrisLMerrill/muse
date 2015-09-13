package org.musetest.core.events;

import org.musetest.core.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class TestErrorEvent extends MuseEvent
    {
    public TestErrorEvent(String message)
        {
        super(MuseEventType.TestExecutionError);
        _message = message;
        }

    public String getDescription()
        {
        return "Unable to execute test: " + _message;
        }

    private String _message;
    }


