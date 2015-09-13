package org.musetest.core.execution;

import org.musetest.core.*;
import org.musetest.core.events.*;
import org.musetest.core.step.*;

import java.util.*;

/**
 * Manages the state of an interactive execution of a stepped test and summarizes test events
 * into InteractiveTestState change events.
 *
 * TODO: could/should this be combined into the InteractiveTestRunner?
 *
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class InteractiveTestController implements MuseEventListener
    {
    public static InteractiveTestController get()
        {
        return CONTROLLER;
        }

    public InteractiveTestState getState()
        {
        return _state;
        }

    public boolean run(SteppedTestProvider test_provider)
        {
        if (_state.equals(InteractiveTestState.IDLE))
            {
            TestRunner runner = TestRunnerFactory.create(test_provider.getProject(), test_provider.getTest(), false, true);
            if (! (runner instanceof InteractiveTestRunner))
                throw new IllegalStateException("Something is wrong...asked for an an InteractiveTestRunner, but didn't get one!");
            _runner = (InteractiveTestRunner) runner;
            setState(InteractiveTestState.STARTING);
            _runner.getTestContext().addEventListener(this);

            setState(InteractiveTestState.RUNNING);
            _runner.runTest();

            return true;
            }
        return false;
        }

    public void addListener(InteractiveTestStateListener listener)
        {
        if (!_listeners.contains(listener))
            _listeners.add(listener);
        }

    public void removeListener(InteractiveTestStateListener listener)
        {
        _listeners.remove(listener);
        }

    @Override
    public void eventRaised(MuseEvent event)
        {
        switch (event.getType())
            {
            case EndTest:
                setState(InteractiveTestState.STOPPING);
                _runner.getTestContext().removeEventListener(this);
                _runner = null;
                setState(InteractiveTestState.IDLE);
                break;
            case Pause:
                setState(InteractiveTestState.PAUSED);
                break;
            case EndStep:
                StepEvent step_event = (StepEvent) event;
                if (_pause_after_any_step)
                    {
                    _runner.requestPause();
                    _pause_after_any_step = false;
                    }
                if (step_event.getConfig() == _pause_after_step)
                    {
                    _runner.requestPause();
                    _pause_after_step = null;
                    }
                break;
            default:
                setState(InteractiveTestState.RUNNING);
            }
        }

    private void setState(InteractiveTestState state)
        {
        if (state.equals(_state))
            return;

        _state = state;
        for (InteractiveTestStateListener listener : _listeners)
            listener.stateChanged(state);
        }

    public TestRunner getTestRunner()
        {
        return _runner;
        }

    public void stop()
        {
        _runner.requestStop();
        }

    public void pause()
        {
        _runner.requestPause();
        }

    public void resume()
        {
        _runner.requestResume();
        }

    public void step()
        {
        _runner.requestStep();
        }

    public void runPastStep(SteppedTestProvider provider, StepConfiguration step)
        {
        _pause_after_step = step;
        run(provider);
        }

    public void runOneStep(SteppedTestProvider provider)
        {
        _pause_after_any_step = true;
        run(provider);
        }

    List<InteractiveTestStateListener> _listeners = new ArrayList<>();

    private InteractiveTestState _state = InteractiveTestState.IDLE;
    private InteractiveTestRunner _runner;
    private boolean _pause_after_any_step = false;
    private StepConfiguration _pause_after_step = null;

    private static InteractiveTestController CONTROLLER = new InteractiveTestController();
    }


