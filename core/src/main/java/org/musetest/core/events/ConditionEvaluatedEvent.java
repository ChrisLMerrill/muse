package org.musetest.core.events;

import org.musetest.core.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class ConditionEvaluatedEvent extends MuseEvent
    {
    public ConditionEvaluatedEvent(String description)
        {
        super(MuseEventType.ConditionEvaluated);
        _description = description;
        }

    @Override
    public String getDescription()
        {
        return _description;
        }

    private String _description;
    }


