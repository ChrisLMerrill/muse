package org.musetest.core.events.matching;

import org.musetest.core.*;
import org.musetest.core.events.*;
import org.musetest.core.step.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class StepResultStatusMatcher implements EventMatcher
    {
    public StepResultStatusMatcher(StepExecutionStatus status)
        {
        _status = status;
        }

    @Override
    public boolean matches(MuseEvent event)
        {
        return event instanceof StepEvent
            && event.getTypeId().equals(StepEvent.EndStepEventType.TYPE_ID)
            && _status.equals(((StepEvent)event).getResult().getStatus());
        }

    private StepExecutionStatus _status;
    }


