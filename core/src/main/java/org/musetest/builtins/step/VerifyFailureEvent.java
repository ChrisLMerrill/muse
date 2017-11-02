package org.musetest.builtins.step;

import org.musetest.core.*;
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
        super(VerifyFailureEventType.TYPE, config, context);
        _message = message;
        }

    @Override
    public String getDescription()
        {
        return _message;
        }

    public boolean isFatal()
        {
        return _fatal;
        }

    public void setFatal(boolean fatal)
        {
        _fatal = fatal;
        }

    private String _message;
    private boolean _fatal = false;

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

	    public final static String TYPE_ID = "VerifyFailed";
	    public final static EventType TYPE = new VerifyFailureEventType();
	    }
    }


