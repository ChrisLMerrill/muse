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
            _provider = test_provider;
            InteractiveTestRunner runner = getRunner();
            setState(InteractiveTestState.STARTING);

            setState(InteractiveTestState.RUNNING);
            runner.runTest();

            return true;
            }
        return false;
        }

    private InteractiveTestRunner getRunner()
        {
        if (_runner == null)
            {
            TestRunner runner = TestRunnerFactory.create(_provider.getProject(), _provider.getTest(), false, true);
            if (! (runner instanceof InteractiveTestRunner))
                throw new IllegalStateException("Something is wrong...asked for an an InteractiveTestRunner, but didn't get one!");
            _runner = (InteractiveTestRunner) runner;
            runner.getTestContext().addEventListener(this);
            runner.getTestContext().addEventListener(new PauseOnError(_runner));
            }
        return _runner;
        }

    public void addListener(InteractiveTestStateListener listener)
        {
        if (!_listeners.contains(listener))
            _listeners.add(listener);
        }

    @SuppressWarnings("unused") // used in GUI
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
                getRunner().getTestContext().removeEventListener(this);
                _runner = null;
                _result = ((EndTestEvent) event).getResult();
                setState(InteractiveTestState.IDLE);
                break;
            case Pause:
                setState(InteractiveTestState.PAUSED);
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
        return getRunner();
        }

    @SuppressWarnings("unused") // used in GUI
    public void stop()
        {
        getRunner().requestStop();
        }

    @SuppressWarnings("unused") // used in GUI
    public void pause()
        {
        getRunner().requestPause();
        }

    public void resume()
        {
        getRunner().requestResume();
        }

    public void step()
        {
        getRunner().requestStep();
        }

    public void runPastStep(SteppedTestProvider provider, StepConfiguration step)
        {
        _provider = provider;
        getRunner().getTestContext().addEventListener(new PauseAfterStep(getRunner(), step));
        run(provider);
        }

    @SuppressWarnings("unused") // used in GUI
    public void runOneStep(SteppedTestProvider provider)
        {
        _provider = provider;
        getRunner().getTestContext().addEventListener(new PauseAfterStep(getRunner()));
        run(provider);
        }

    public MuseTestResult getResult()
        {
        return _result;
        }

    private List<InteractiveTestStateListener> _listeners = new ArrayList<>();

    private InteractiveTestState _state = InteractiveTestState.IDLE;
    private SteppedTestProvider _provider;
    private InteractiveTestRunner _runner;
    private MuseTestResult _result = null;

    private static InteractiveTestController CONTROLLER = new InteractiveTestController();
    }


