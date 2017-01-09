package org.musetest.core.execution;

import org.musetest.core.*;
import org.musetest.core.events.*;
import org.musetest.core.step.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@SuppressWarnings("WeakerAccess")  // used by GUI
public class PauseAfterStep implements MuseEventListener
    {
    public PauseAfterStep(InteractiveTestRunner runner)
        {
        _runner = runner;
        _step = null;
        }

    public PauseAfterStep(InteractiveTestRunner runner, StepConfiguration step)
        {
        _runner = runner;
        _step = step;
        }

    @Override
    public void eventRaised(MuseEvent event)
        {
        if (event.getType().equals(MuseEventType.EndStep))
            {
            StepEvent step_event = (StepEvent) event;
            if (_step == null || step_event.getConfig() == _step)
                {
                _runner.requestPause();
                _runner.getExecutionContext().removeEventListener(this);
                }
            }
        }

    private final InteractiveTestRunner _runner;
    private final StepConfiguration _step;
    }


