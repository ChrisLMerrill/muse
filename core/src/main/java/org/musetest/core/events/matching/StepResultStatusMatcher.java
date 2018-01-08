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
        if (!event.getTypeId().equals(EndStepEventType.TYPE_ID))
        	return false;

        if (_status.equals(StepExecutionStatus.ERROR))
	        return event.hasTag(MuseEvent.ERROR);
        if (_status.equals(StepExecutionStatus.FAILURE))
	        return event.hasTag(MuseEvent.FAILURE);
        return true;
        }

    private StepExecutionStatus _status;
    }
