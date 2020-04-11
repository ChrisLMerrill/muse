package org.museautomation.core.task.state;

import javax.annotation.*;
import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class BasicStateContainer implements StateContainer
    {
    @Override
    public void addState(@Nonnull InterTaskState state)
        {
        if (!_states.contains(state))
            {
            _states.add(state);
            for (StateContainerChangeListener listener : _listeners)
                listener.stateAdded(state);
            }
        }

    @Override
    public void removeState(@Nonnull InterTaskState state)
        {
        if (_states.contains(state))
            {
            _states.remove(state);
            for (StateContainerChangeListener listener : _listeners)
                listener.stateRemoved(state);
            }
        }

    @Override
    public void replaceState(@Nonnull InterTaskState old_state, @Nonnull InterTaskState new_state)
        {
        if (!_states.contains(old_state))
            throw new IllegalStateException("Cannot replace state because it is not found");
        _states.remove(old_state);
        _states.add(new_state);
        for (StateContainerChangeListener listener : _listeners)
            listener.stateReplaced(old_state, new_state);
        }

    public List<InterTaskState> findStates(String type_id)
        {
        List<InterTaskState> matches = new ArrayList<>();
        for (InterTaskState state : _states)
            if (type_id.equals(state.getStateDefinitionId()))
                matches.add(state);
        return matches;
        }

    public InterTaskState findState(String type_id)
        {
        List<InterTaskState> matches = findStates(type_id);
        if (matches.size() != 1)
            return null;
        return matches.get(0);
        }

    @Override
    public Iterator<InterTaskState> states()
        {
        return _states.iterator();
        }

    @Override
    public void addStateListener(StateContainerChangeListener listener)
        {
        _listeners.add(listener);
        }

    @Override
    public void removeStateListener(StateContainerChangeListener listener)
        {
        _listeners.remove(listener);
        }

    @Override
    public boolean contains(InterTaskState state)
        {
        return _states.contains(state);
        }

    @Override
    public int size()
        {
        return _states.size();
        }

    private final List<StateContainerChangeListener> _listeners = new ArrayList<>();
    private final Set<InterTaskState> _states = new HashSet<>();
    }