package org.musetest.core.mocks;

import org.musetest.core.context.*;
import org.musetest.core.events.*;
import org.musetest.core.step.*;

/**
 * A mock that allows overriding the timestamp for testing.
 *
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@SuppressWarnings("unused")  // used in some other packages depending on this
public class MockStepEvent extends StepEvent
    {
    public MockStepEvent(EventType type, StepConfiguration config, StepExecutionContext context)
        {
        super(type, config, context);
        }

    public void setResult(StepExecutionResult result)
        {
        _result = result;
        }

    public void setTimestampNanos(long nanos)
        {
        _timestamp_nanos = nanos;
        }
    }


