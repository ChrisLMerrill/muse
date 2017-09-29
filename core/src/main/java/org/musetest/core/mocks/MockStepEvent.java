package org.musetest.core.mocks;

import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.events.*;
import org.musetest.core.step.*;

/**
 * A mock that allows overriding the timestamp for testing.
 *
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class MockStepEvent extends StepEvent
    {
    public MockStepEvent(MuseEventType type, StepConfiguration config, StepExecutionContext context)
        {
        super(type, config, context);
        }

    public void setTimestampNanos(long nanos)
        {
        _timestamp_nanos = nanos;
        }
    }


