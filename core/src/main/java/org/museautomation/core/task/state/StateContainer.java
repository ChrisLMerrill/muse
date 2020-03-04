package org.museautomation.core.task.state;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public interface StateContainer
    {
    void addState(InterTaskState state);
    void removeState(InterTaskState state);
    Iterator<InterTaskState> states();
    List<InterTaskState> findStates(String type_id);
    void addStateListener(StateContainerChangeListener listener);
    void removeStateListener(StateContainerChangeListener listener);

    interface StateContainerChangeListener
        {
        void stateAdded(InterTaskState state);
        void stateRemoved(InterTaskState state);
        }
    }