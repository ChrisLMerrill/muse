package org.musetest.builtins.step;

import org.musetest.core.context.*;
import org.musetest.core.events.*;
import org.musetest.core.step.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class VerifyFailureEvent extends StepEvent
    {
    public VerifyFailureEvent(StepConfiguration config, StepExecutionContext context, String message)
        {
        super(VerifyFailureEventType.INSTANCE, config, context);
        _message = message;
        setStatus(EventStatus.Failure);
        }

    @Override
    public String getDescription()
        {
        return _message;
        }

    private String _message;

    public static class VerifyFailureEventType extends EventType
	    {
	    @Override
	    public String getTypeId()
		    {
		    return TYPE_ID;
		    }

	    @Override
	    public String getName()
		    {
		    return "Verify Failed";
		    }

	    public final static String TYPE_ID = "verify-failed";
	    public final static EventType INSTANCE = new VerifyFailureEventType();
	    }
    }


