package org.musetest.core.events;

import org.musetest.core.*;
import org.musetest.core.step.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@SuppressWarnings("unused")  // used by InteractiveTestRunner
public class PauseTestEvent extends MuseEvent
    {
    @SuppressWarnings("unused")  // used by InteractiveTestRunner
    public PauseTestEvent(StepConfiguration next_step)
        {
        super(PauseTestEventType.INSTANCE);
        _next_step = next_step;
        }

    @SuppressWarnings("unused")  // used in UI
    public StepConfiguration getNextStep()
        {
        return _next_step;
        }

    private StepConfiguration _next_step;

    public final static class PauseTestEventType extends EventType
	    {
	    @Override
	    public String getTypeId()
		    {
		    return TYPE_ID;
		    }

	    @Override
	    public String getName()
		    {
		    return "Pause";
		    }

	    public final static String TYPE_ID = "pause";
	    public final static EventType INSTANCE = new PauseTestEventType();
	    }

    }