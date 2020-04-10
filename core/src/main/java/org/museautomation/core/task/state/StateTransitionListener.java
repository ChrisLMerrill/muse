package org.museautomation.core.task.state;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public interface StateTransitionListener
    {
    void transitionEvent(StateTransitionEvent e);
    }