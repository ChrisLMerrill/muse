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
        super(MuseEventType.VerifyFailed, config, context);
        _message = message;
        }

    @Override
    public String getDescription()
        {
        return _message;
        }

    private String _message;
    }


