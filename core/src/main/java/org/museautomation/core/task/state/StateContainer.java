package org.museautomation.core.task.state;

import javax.annotation.*;
import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public interface StateContainer
    {
    void addState(InterTaskState state);
    void removeState(InterTaskState state);
    void replaceState(InterTaskState new_state, InterTaskState old_state);
    Iterator<InterTaskState> states();
    List<InterTaskState> findStates(String type_id);
    InterTaskState findState(String type_id) throws IllegalStateException;
    void addStateListener(StateContainerChangeListener listener);
    void removeStateListener(StateContainerChangeListener listener);
    boolean contains(InterTaskState state);
    int size();

    interface StateContainerChangeListener
        {
        void stateAdded(@Nonnull InterTaskState state);
        void stateRemoved(@Nonnull InterTaskState state);
        void stateReplaced(@Nonnull InterTaskState old_state, @Nonnull InterTaskState new_state);
        }
    }