package org.museautomation.core.task.state;

import org.jetbrains.annotations.*;

import javax.annotation.*;
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
        void stateAdded(@Nonnull InterTaskState state);
        void stateRemoved(@Nonnull InterTaskState state);
        }
    }