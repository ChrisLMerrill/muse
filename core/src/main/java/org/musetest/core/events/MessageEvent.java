package org.musetest.core.events;

import org.musetest.core.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class MessageEvent extends MuseEvent
    {
    public MessageEvent(String message)
        {
        super(MuseEventType.Message);
        _message = message;
        }

    @Override
    public String getDescription()
        {
        return _message;
        }

    private String _message;
    }


