package org.museautomation.core.events.matching;

import org.museautomation.core.*;
import org.museautomation.core.events.*;
import org.museautomation.core.step.*;

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
