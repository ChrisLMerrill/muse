package org.musetest.core.execution;

/**
 * Blocks until the test achieves the desired state
 *
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public  class TestStateBlocker implements InteractiveTestStateListener
    {
    public TestStateBlocker(InteractiveTestController controller)
        {
        _controller = controller;
        _controller.addListener(this);
        }

    public synchronized void blockUntil(InteractiveTestState state)
        {
        if (_controller.getState().equals(state))
            return;

        _state = state;
        try
            {
            wait();
            }
        catch (InterruptedException e)
            {
            // ok
            }

        }

    @Override
    public synchronized void stateChanged(InteractiveTestState state)
        {
        if (state.equals(_state))
            notify();
        }

    private InteractiveTestController _controller;
    private InteractiveTestState _state;
    }


