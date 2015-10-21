package org.musetest.core.tests.utils;

import org.junit.*;
import org.musetest.core.execution.*;

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
            wait(2000);
            }
        catch (InterruptedException e)
            {
            Assert.assertTrue("gave up waiting after 2 sec...it probably failed if this was used (as intended) for a should-finish-more-or-less-instantly operation in a unit test.", false);
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


