package org.musetest.core.events;

import org.musetest.core.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class ValueSourceResolvedEvent extends MuseEvent
    {
    public ValueSourceResolvedEvent(String description, Object value)
        {
        super(MuseEventType.ValueResolved);
        _description = description;
        _value = value;
        }

    @Override
    public String getDescription()
        {
        return "Resolved " + _description + " = " + _value;
        }

    private String _description;
    private Object _value;
    }


